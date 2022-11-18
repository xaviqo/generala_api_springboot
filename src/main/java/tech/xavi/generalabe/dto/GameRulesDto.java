package tech.xavi.generalabe.dto;

import lombok.Data;
import tech.xavi.generalabe.model.TimeRule;

@Data
public class GameRulesDto {

    private int maxPlayers;
    private TimeRule timeRule;
    private String password;

}
