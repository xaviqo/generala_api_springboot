package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.repository.GameRepository;

@Service
@AllArgsConstructor
public class CommonGameService {

    private final GameRepository gameRepository;

    public Game findGameByLobbyId(String lobbyId){
        return gameRepository
                .findByLobbyId(lobbyId)
                .orElseThrow(() -> new GeneralaException
                        (GeneralaError.GameIdNotFound,
                                HttpStatus.BAD_REQUEST));
    }

    public Game updateGame(Game game){
        return gameRepository.save(game);
    }

    public void removeGame(Game game) {
        gameRepository.delete(game);
    }
}
