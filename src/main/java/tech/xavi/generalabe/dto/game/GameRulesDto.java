package tech.xavi.generalabe.dto.game;

import lombok.Builder;
import lombok.Data;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.model.TimeRule;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class GameRulesDto {

    private String gameAdmin;
    private int maxPlayers;
    private TimeRule timeRule;
    private boolean openToEveryone;
    private String password;
    private int lobbySize;
    private Map<String,String> idsAndPlayers;
    private LinkedHashSet<String> alreadyConnectedPlayers;

}
