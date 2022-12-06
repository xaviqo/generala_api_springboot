package tech.xavi.generalabe.dto.game.score;

import lombok.Builder;
import lombok.Data;
import tech.xavi.generalabe.model.Scores;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Builder
@Data
public class ScoreTableDto {
    private int round;
    private int turn;
    Map<String,String> idAndNickname;
    Set<Scores> scores;
    List<String> categories;
}
