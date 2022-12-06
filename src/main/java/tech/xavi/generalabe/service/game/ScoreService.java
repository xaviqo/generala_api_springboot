package tech.xavi.generalabe.service.game;

import com.sun.source.tree.Tree;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.game.score.ScoreTableDto;
import tech.xavi.generalabe.model.CombinationCategory;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.model.Scores;
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
        List<String> categories = new ArrayList<>();
        Map<String,String> idAndNickname = new HashMap<>();

        game.getPlayers().forEach( player -> {
            idAndNickname.put(player.getId(), player.getNickname());
        });

        for (CombinationCategory cc: CombinationCategory.values())
            categories.add(cc.getCategory());

        return ScoreTableDto.builder()
                .round(game.getRound())
                .turn(game.getTurn())
                .scores(game.getScoreTable())
                .categories(categories)
                .idAndNickname(idAndNickname)
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

        LinkedHashSet<Scores> gameScores = new LinkedHashSet<>();
        Set<Player> players = game.getPlayers();

        for (CombinationCategory category : CombinationCategory.values()) {
            Scores scores = new Scores(category);
            players.forEach(scores::setUserScore);
            gameScores.add(scores);
        }

        game.setScoreTable(gameScores);
        commonGameService.updateGame(game);
    }

    private int calculateTotal(Player player){
        return 1;
    }

    private int calculateSubtotal(Player player){
        return 1;
    }
}
