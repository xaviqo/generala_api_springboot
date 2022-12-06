package tech.xavi.generalabe.utils.game;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tech.xavi.generalabe.model.TimeRule;
import tech.xavi.generalabe.service.game.TimerService;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Data
@Slf4j
public class GameTimer{

    private String gameId;
    private TimeRule timeRule;
    private boolean ready;
    private boolean run;
    private Map<String,Boolean> playerOnline;
    private Set<String> playerIds;

    public GameTimer() {
        playerOnline = new HashMap<>();
        playerIds = new HashSet<>();
    }

    public void startTurn() {
        int sec = this.timeRule.getTime();
        try {
            TimerService.timerMessage(this.gameId,"start");
            while (sec > 0 && run){
                Thread.sleep(Duration.ofSeconds(1));
                sec--;
            }
            TimerService.timerMessage(this.gameId,"stop");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkPlayerReady(){
        for (boolean onlinePlayer : playerOnline.values()) {
            if (!onlinePlayer) return false;
        }
        return true;
    }

    public void playerConnected(String playerId){
        playerOnline.put(playerId,true);
        setReady(checkPlayerReady());
    }

    public void playerDisconnected(String playerId){
        playerOnline.put(playerId,false);
    }

    public void setPlayerId(String id){
        playerIds.add(id);
    }

    public void initializePlayersOnline(){
        playerIds.forEach( p -> {
            playerOnline.put(p,false);
        });
    }

}
