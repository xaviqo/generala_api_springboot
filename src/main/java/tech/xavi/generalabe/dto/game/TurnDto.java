package tech.xavi.generalabe.dto.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TurnDto {

    private String playerTurnNickname;
    private String playerTurnId;
    boolean isMyTurn;
    int gameTurn;
    int gameRound;
}
