package tech.xavi.generalabe.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.exception.GeneralaException;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.model.TimeRule;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
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
    private LinkedHashSet<String> websocketLobbyRegistry;
    private Set<String> kickedPlayers;

    public boolean isOpenToEveryone(){
        return getPassword().isBlank();
    }

    public boolean setNewPlayer(Player player){
        if (!players.contains(player)){
            return this.players.add(player);
        }
        return false;
    }

    public boolean removePlayer(String playerId){
        removeWebsocketLobbyRegistry(playerId);
        return this.players.removeIf( p -> p.getId().equalsIgnoreCase(playerId));
    }

    public void removeWebsocketLobbyRegistry(String userId){
        websocketLobbyRegistry.remove(userId);
    }

    public void setWebsocketLobbyRegistry(String userId) {
        this.websocketLobbyRegistry.add(userId);
    }

    public boolean isAvailable(){
        return ((getMaxPlayers() > getSubscribedPlayers()) &&
                !isStarted() && isConfigured());
    }

    public String getAdminNickname(){
        for (Player player : players){
            if (player.getId().equals(adminId)) return player.getNickname();
        }
        throw new GeneralaException(GeneralaError.GeneralError, HttpStatus.INTERNAL_SERVER_ERROR);
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
