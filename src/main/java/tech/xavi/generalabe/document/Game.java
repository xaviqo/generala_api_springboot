package tech.xavi.generalabe.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.model.TimeRule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    private String lobbyId;
    private String adminId;
    private int turn; // MAX = GAME_PLAYERS*MAX_ROUNDS
    private int round;
    private TimeRule timeRule;
    private int maxPlayers;
    private List<Player> players;
    private String password;
    private LocalDateTime dateTimeCreated;
    private boolean configured;
    private boolean started;
    private boolean finished;
    @JsonIgnore
    private Set<String> websocketLobbyRegistry;

    public boolean setNewPlayer(Player player){
        if (!players.contains(player)){
            return this.players.add(player);
        }
        return false;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @JsonIgnore
    public int getSubscribedPlayers(){
        return this.players.size();
    }
    @JsonIgnore
    public int getNextIngameId(){
        return getSubscribedPlayers();
    }
}
