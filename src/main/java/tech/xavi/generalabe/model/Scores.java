package tech.xavi.generalabe.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Scores {
    private CombinationCategory combinationCategory;
    private String category;
    Map<String,Integer> userScore;

    public Scores(CombinationCategory combinationCategory) {
        this.combinationCategory = combinationCategory;
        this.category = combinationCategory.getCategory();
        this.userScore = new HashMap<>();
    }

    public void setUserScore(Player player) {
        userScore.put(player.getId(), player.getScoreSheet().get(combinationCategory));
    }
}
