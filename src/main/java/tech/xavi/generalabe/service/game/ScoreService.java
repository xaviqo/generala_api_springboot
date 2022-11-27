package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.game.score.ScoreTableDto;
import tech.xavi.generalabe.model.CombinationCategory;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.service.user.CommonUserService;

import java.util.*;

@AllArgsConstructor
@Service
public class ScoreService {

    private final CommonGameService commonGameService;
    private final CommonUserService commonUserService;

    public ScoreTableDto getGameScoreTable(){

        GeneralaUser user = commonUserService.getAuthenticatedPlayer();
        Game game = commonGameService.findGameByLobbyId(user.getLobby());
        updateGameScores(game);

        List<Map<String,String>> idsAndNicknames = new ArrayList<>();
        List<Map<String,Object>> scores = new ArrayList<>();

        // headers for vue datatable
        game.getPlayers().forEach( player -> {
            Map<String,String> textValue = new HashMap<>();
            textValue.put("text",player.getNickname());
            textValue.put("value",player.getId());
            idsAndNicknames.add(textValue);
        });

        // items for vue datatable
        game.getScoreTable().forEach( (category,usersScores) -> {
            Map<String, Object> scoreKeyValues = new HashMap<>(usersScores);
            scoreKeyValues.put("sortBy",category.getSort());
            scoreKeyValues.put("name",category.getCategory());
            scores.add(scoreKeyValues);
        });

        return ScoreTableDto.builder()
                .round(game.getRound())
                .turn(game.getTurn())
                .idAndNickname(idsAndNicknames)
                .scores(scores)
                .build();
    }

    public Map<CombinationCategory,Integer> generatePlayerScoreSheet(){
        Map<CombinationCategory,Integer> playerScoreMap = new HashMap<>();
        for (CombinationCategory category : CombinationCategory.values()) {
            playerScoreMap.put(category,0);
        }
        return playerScoreMap;
    }

    private void updateGameScores(Game game){

        Map<CombinationCategory, Map<String,Integer>> scoreTable = new HashMap<>();
        Set<Player> players = game.getPlayers();

        for (CombinationCategory category : CombinationCategory.values()) {
            Map<String,Integer> playerScoreForEachRow = new HashMap<>();
            players.forEach( player -> {
                playerScoreForEachRow.put(player.getId(),player.getScoreSheet().get(category));
            });
            scoreTable.put(category,playerScoreForEachRow);
        }

        game.setScoreTable(scoreTable);
        commonGameService.updateGame(game);
    }

    private int calculateTotal(Player player){
        return 1;
    }

    private int calculateSubtotal(Player player){
        return 1;
    }
}
