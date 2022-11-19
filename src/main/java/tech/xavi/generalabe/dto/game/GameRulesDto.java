package tech.xavi.generalabe.dto.game;

import lombok.Builder;
import lombok.Data;
import tech.xavi.generalabe.model.TimeRule;

@Data
@Builder
public class GameRulesDto {

    private String gameAdmin;
    private int maxPlayers;
    private TimeRule timeRule;
    private boolean openToEveryone;
    private String password;
    private int lobbySize;

}
