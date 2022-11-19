package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.game.GameRulesDto;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.service.user.CommonUserService;

@AllArgsConstructor
@Service
public class GameRulesService {

    private final CommonGameService commonGameService;

    private final CommonUserService commonUserService;

    public GameRulesDto getGameRules(String lobbyId){
        GeneralaUser user = commonUserService.getAuthenticatedPlayer();
        checkValid_lobbyId(lobbyId,user.getLobby());
        Game game = commonGameService.findGameByLobbyId(user.getLobby());

        return GameRulesDto.builder()
                .gameAdmin(game.getAdminNickname())
                .maxPlayers(game.getMaxPlayers())
                .timeRule(game.getTimeRule())
                .openToEveryone(game.isOpenToEveryone())
                .lobbySize(game.getMaxPlayers())
                .build();
    }



    public Game configureGame(String lobbyId, GameRulesDto dto){
        GeneralaUser admin = commonUserService.getAuthenticatedPlayer();
        checkValid_lobbyId(lobbyId,admin.getLobby());
        checkValid_configureGameDto(dto);
        Game game = commonGameService.findGameByLobbyId(admin.getLobby());

        if (game.isConfigured())
            throw new GeneralaException(GeneralaError.GameAlreadyConfigured, HttpStatus.FORBIDDEN);

        game.setMaxPlayers(dto.getMaxPlayers());
        game.setTimeRule(dto.getTimeRule());
        game.setPassword(dto.getPassword());
        game.setConfigured(true);

        commonGameService.updateGame(game);

        return game;
    }

    private void checkValid_configureGameDto(GameRulesDto dto){

        // ENUM TIME_RULE IS ALREADY VALIDATED BY JACKSON
        if (dto.getMaxPlayers() < Global.MIN_PLAYERS || dto.getMaxPlayers() > Global.MAX_PLAYERS){
            throw new GeneralaException(GeneralaError.InvalidAmountPlayers, HttpStatus.BAD_REQUEST);
        }

        if (!dto.getPassword().isBlank() && dto.isOpenToEveryone()){
            if (dto.getPassword().length() < Global.MIN_GAME_PWD_LENGTH
                    || dto.getPassword().length() > Global.MAX_GAME_PWD_LENGTH){
                throw new GeneralaException(GeneralaError.InvalidPasswordLength, HttpStatus.BAD_REQUEST);
            }
        }

    }

    public void checkValid_lobbyId(String lobbyId,String userLobbyId){
        if (!lobbyId.equalsIgnoreCase(userLobbyId))
            throw new GeneralaException(GeneralaError.GameAlreadyConfigured, HttpStatus.FORBIDDEN);
    }

}
