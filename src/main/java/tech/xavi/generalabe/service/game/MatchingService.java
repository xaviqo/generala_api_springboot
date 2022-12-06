package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.auth.TokenPayload;
import tech.xavi.generalabe.dto.lobby.KickPlayerDto;
import tech.xavi.generalabe.dto.lobby.LocalStorageSessionDto;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.model.LobbyInteraction;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.service.user.AuthService;
import tech.xavi.generalabe.service.user.CommonUserService;
import tech.xavi.generalabe.service.user.UserService;
import tech.xavi.generalabe.service.websocket.WebSocketChatService;
import tech.xavi.generalabe.service.websocket.WebSocketConnectionService;
import tech.xavi.generalabe.utils.UUIDGenerala;
import tech.xavi.generalabe.utils.game.GameTimer;
import tech.xavi.generalabe.utils.mapper.PlayerUserMapper;

@Slf4j
@AllArgsConstructor
@Service
public class MatchingService {

    private final UserService userService;
    private final CommonUserService commonUserService;
    private final CommonGameService commonGameService;
    private final AuthService authService;
    private final GameStatusService gameStatusService;
    private final WebSocketChatService webSocketChatService;
    private final WebSocketConnectionService webSocketConnectionService;
    private final ScoreService scoreService;

    public LocalStorageSessionDto prepareGameAndAdmin(String nickname){

        String password = UUIDGenerala.random8Chars();

        GeneralaUser admin = userService.createUserAndSave(nickname,password);
        Game game = gameStatusService.initGame(admin);
        admin.setLobby(game.getLobbyId());
        commonUserService.updateUserData(admin);

        TokenPayload tokenPayload = authService.setAuthentication(admin.getId(), password);

        return LocalStorageSessionDto.builder()
                .nickname(admin.getNickname())
                .lobbyId(admin.getLobby())
                .password(password)
                .playerId(admin.getId())
                .tokenPayload(tokenPayload)
                .build();

    }

    public LocalStorageSessionDto joinGameAndPrepareUser(String lobbyId, String nickname){

        //check lobby configured but game not started&&finished

        Game game = commonGameService.findGameByLobbyId(lobbyId);

        if (!game.isAvailable())
            throw new GeneralaException(GeneralaError.GameStartedOrFinished, HttpStatus.FORBIDDEN);

        String password = UUIDGenerala.random8Chars();
        GeneralaUser user = userService.createUserAndSave(nickname,password);
        user.setLobby(lobbyId);
        commonUserService.updateUserData(user);

        Player player = PlayerUserMapper.toPlayer(user);
        player.setIngameId(game.getNextIngameId());
        player.setScoreSheet(scoreService.generatePlayerScoreSheet());
        game.setNewPlayer(player);
        commonGameService.updateGame(game);

        TokenPayload tokenPayload = authService.setAuthentication(user.getId(), password);

        return LocalStorageSessionDto.builder()
                .nickname(user.getNickname())
                .lobbyId(user.getLobby())
                .password(password)
                .playerId(user.getId())
                .tokenPayload(tokenPayload)
                .build();
    }

    public void kickPlayer(KickPlayerDto dto){
        GeneralaUser admin = commonUserService.getAuthenticatedPlayer();
        Game game = commonGameService.findGameByLobbyId(admin.getLobby());

        if (!game.getAdminId().equalsIgnoreCase(admin.getId()))
            throw new GeneralaException(GeneralaError.NotGameAdmin,HttpStatus.FORBIDDEN);

        if (!admin.getLobby().equalsIgnoreCase(dto.getLobbyId()))
            throw new GeneralaException(GeneralaError.InvalidGameId,HttpStatus.FORBIDDEN);

        if (!game.removePlayer(dto.getPlayerToKickId()))
            throw new GeneralaException(GeneralaError.ErrorKickLobby,HttpStatus.INTERNAL_SERVER_ERROR);

        GeneralaUser userToKick = commonUserService.findByUserId(dto.getPlayerToKickId());

        webSocketConnectionService.sendActivityMessage(userToKick.getLobby(),userToKick.getNickname(), userToKick.getId(), LobbyInteraction.KICK);
        commonGameService.updateGame(game);
        webSocketChatService.removeWsSpamFilter(dto.getPlayerToKickId(), admin.getLobby());
        commonUserService.removeUser(userToKick);

    }

    public void startGame(){

        GeneralaUser admin = commonUserService.getAuthenticatedPlayer();
        Game game = commonGameService.findGameByLobbyId(admin.getLobby());

        if (game.isStarted() || game.isFinished())
            throw new GeneralaException(GeneralaError.GameStartedOrFinished,HttpStatus.FORBIDDEN);

        if (!game.getAdminId().equalsIgnoreCase(admin.getId()))
            throw new GeneralaException(GeneralaError.NotGameAdmin,HttpStatus.FORBIDDEN);

        if (game.getWebsocketLobbyRegistry().size()<2){
            throw new GeneralaException(GeneralaError.NotEnoughPlayers, HttpStatus.FORBIDDEN);
        }

        game.setGameTimer(new GameTimer());
        game.setStarted(true);
        commonGameService.updateGame(game);

        webSocketConnectionService.sendActivityMessage(game.getLobbyId(),"", "", LobbyInteraction.START);

    }

    public void leaveLobby(String lobbyId, String nickname){

        GeneralaUser user = commonUserService.getAuthenticatedPlayer();

        if (!user.getNickname().equalsIgnoreCase(nickname))
            throw new GeneralaException(GeneralaError.InvalidNicknameIndicated,HttpStatus.FORBIDDEN);

        if (!user.getLobby().equalsIgnoreCase(lobbyId))
            throw new GeneralaException(GeneralaError.InvalidGameId,HttpStatus.FORBIDDEN);

        Game game = commonGameService.findGameByLobbyId(user.getLobby());

        if (!game.removePlayer(user.getId()))
            throw new GeneralaException(GeneralaError.ErrorLeaveLobby,HttpStatus.INTERNAL_SERVER_ERROR);

        commonGameService.updateGame(game);
        webSocketConnectionService.sendActivityMessage(user.getLobby(),user.getNickname(), user.getId(), LobbyInteraction.LEAVE);
        webSocketChatService.removeWsSpamFilter(user.getId(), user.getLobby());
        commonUserService.removeUser(user);

    }

    public void deleteLobby() {

        GeneralaUser admin = commonUserService.getAuthenticatedPlayer();
        Game game = commonGameService.findGameByLobbyId(admin.getLobby());

        if (!game.getAdminId().equalsIgnoreCase(admin.getId()))
            throw new GeneralaException(GeneralaError.NotGameAdmin,HttpStatus.FORBIDDEN);

        webSocketConnectionService.sendActivityMessage(game.getLobbyId(), admin.getNickname(), admin.getId(), LobbyInteraction.DELETE);

        try {
            Thread.sleep(1200);
        } catch (Exception e) {
            e.printStackTrace();
        }

        game.getPlayers().forEach( p -> {
            webSocketChatService.removeWsSpamFilter(p.getId(), game.getLobbyId());
            commonUserService.removeUser(commonUserService.findByUserId(p.getId()));
        });

        commonGameService.removeGame(game);

    }



}
