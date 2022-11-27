package tech.xavi.generalabe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class Player {

    private String id;
    private String nickname;
    private int ingameId;
    private Map<CombinationCategory,Integer> scoreSheet;
    @JsonIgnore
    private DiceCup diceCup;

}