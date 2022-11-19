package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.auth.TokenPayload;
import tech.xavi.generalabe.dto.lobby.LocalStorageSessionDto;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.repository.GameRepository;
import tech.xavi.generalabe.service.user.AuthService;
import tech.xavi.generalabe.service.user.CommonUserService;
import tech.xavi.generalabe.service.user.UserService;
import tech.xavi.generalabe.utils.UUIDGenerala;
import tech.xavi.generalabe.utils.mapper.PlayerUserMapper;

@AllArgsConstructor
@Service
public class MatchingService {

    private final UserService userService;
    private final CommonUserService commonUserService;
    private final CommonGameService commonGameService;
    private final AuthService authService;

    private final GameStatusService gameStatusService;

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


}
