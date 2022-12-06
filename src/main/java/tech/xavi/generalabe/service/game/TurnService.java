package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.game.TurnDto;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.service.user.CommonUserService;

import java.util.Objects;

@Service
@AllArgsConstructor
public class TurnService {

    private final CommonGameService commonGameService;
    private final CommonUserService commonUserService;

    public TurnDto getTurn(){
        GeneralaUser user = commonUserService.getAuthenticatedPlayer();
        Game game = commonGameService.findGameByLobbyId(user.getLobby());
        Player player = game.getPlayerWhoPlays();

        if (player == null)
            throw new GeneralaException(GeneralaError.ErrorCheckingTurn, HttpStatus.INTERNAL_SERVER_ERROR);

        return TurnDto.builder()
                .isMyTurn(Objects.equals(player.getId(), user.getId()))
                .playerTurnNickname(player.getNickname())
                .playerTurnId(player.getId())
                .gameTurn(game.getTurn())
                .gameRound(game.getRound())
                .build();
    }

}
