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

    public Game findGameById(String gameId){
        return gameRepository
                .findById(gameId)
                .orElseThrow(() -> new GeneralaException
                        (GeneralaError.GameIdNotFound,
                                HttpStatus.BAD_REQUEST));
    }

    public Game updateGameStatus(Game game){
        return gameRepository.save(game);
    }


}
