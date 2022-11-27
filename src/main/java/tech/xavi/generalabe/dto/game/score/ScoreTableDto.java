package tech.xavi.generalabe.dto.game.score;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class ScoreTableDto {
    private int round;
    private int turn;
    List<Map<String,String>> idAndNickname;
    List<Map<String,Object>> scores;
}
