package tech.xavi.generalabe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Player {

    private String id;
    private String nickname;
    private int ingameId;
    private ScoreSheet scoreSheet;
    @JsonIgnore
    private DiceCup diceCup;

}