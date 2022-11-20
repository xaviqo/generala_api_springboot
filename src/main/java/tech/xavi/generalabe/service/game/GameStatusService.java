package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.repository.GameRepository;
import tech.xavi.generalabe.utils.UUIDGenerala;
import tech.xavi.generalabe.utils.mapper.PlayerUserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

@AllArgsConstructor
@Service
public class GameStatusService {

    private final GameRepository gameRepository;

    public Game initGame(GeneralaUser admin){

        Game game = Game.builder()
                .lobbyId(UUIDGenerala.random8Chars())
                .adminId(admin.getId())
                .turn(0)
                .round(0)
                .maxPlayers(2)
                .players(new ArrayList<>())
                .dateTimeCreated(LocalDateTime.now())
                .configured(false)
                .started(false)
                .finished(false)
                .websocketLobbyRegistry(new LinkedHashSet<>())
                .kickedPlayers(new LinkedHashSet<>())
                .build();

        Player player = PlayerUserMapper.toPlayer(admin);
        player.setIngameId(game.getNextIngameId());
        game.setNewPlayer(player);

        return gameRepository.save(game);
    }




}
