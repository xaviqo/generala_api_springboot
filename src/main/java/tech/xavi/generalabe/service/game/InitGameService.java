package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.GameRulesDto;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.repository.GameRepository;
import tech.xavi.generalabe.service.user.CommonUserService;
import tech.xavi.generalabe.service.user.UserService;
import tech.xavi.generalabe.utils.UUIDGenerala;
import tech.xavi.generalabe.utils.mapper.PlayerUserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@AllArgsConstructor
@Service
public class InitGameService {

    private final GameRepository gameRepository;

    private final CommonGameService commonGameService;

    private final CommonUserService commonUserService;

    public Game initGame(GeneralaUser admin){

        Game game = Game.builder()
                .lobbyId(UUIDGenerala.generateLobbyId())
                .adminId(admin.getId())
                .turn(0)
                .round(0)
                .maxPlayers(2)
                .players(new ArrayList<>())
                .dateTimeCreated(LocalDateTime.now())
                .configured(false)
                .started(false)
                .finished(false)
                .websocketLobbyRegistry(new HashSet<>())
                .build();

        Player player = PlayerUserMapper.toPlayer(admin);
        player.setIngameId(game.getNextIngameId());
        game.setNewPlayer(player);

        return gameRepository.save(game);
    }

    public Game configureGame(GameRulesDto dto){
        GeneralaUser admin = commonUserService.getAuthenticatedPlayer();
        checkValid_configureGameDto(dto);
        Game game = commonGameService.findGameById(admin.getLobby());

        game.setMaxPlayers(dto.getMaxPlayers());
        game.setTimeRule(dto.getTimeRule());
        game.setPassword(dto.getPassword());
        game.setConfigured(true);

        commonGameService.updateGameStatus(game);

        return game;
    }

    private void checkValid_configureGameDto(GameRulesDto dto){

        // ENUM TIME_RULE IS ALREADY VALIDATED BY JACKSON
        if (dto.getMaxPlayers() < Global.MIN_PLAYERS || dto.getMaxPlayers() > Global.MAX_PLAYERS){
            throw new GeneralaException(GeneralaError.InvalidAmountPlayers, HttpStatus.BAD_REQUEST);
        }

        if (dto.getPassword().length() < Global.MIN_GAME_PWD_LENGTH
                || dto.getPassword().length() > Global.MAX_GAME_PWD_LENGTH){
            throw new GeneralaException(GeneralaError.InvalidPasswordLength, HttpStatus.BAD_REQUEST);
        }

    }

}
