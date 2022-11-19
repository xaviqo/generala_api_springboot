package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.lobby.UserLobbyConnectionStatus;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.service.user.CommonUserService;

import java.util.*;

@AllArgsConstructor
@Service
public class LobbyService {

    private final CommonUserService commonUserService;
    private final CommonGameService commonGameService;

    public List<UserLobbyConnectionStatus> usersInLobby(){

        List<UserLobbyConnectionStatus> connectedPlayers = new ArrayList<>();
        GeneralaUser generalaUser = commonUserService.getAuthenticatedPlayer();
        Game game = commonGameService.findGameByLobbyId(generalaUser.getLobby());

        for (Player player : game.getPlayers()){
            UserLobbyConnectionStatus userStatus = new UserLobbyConnectionStatus();
            userStatus.setId(player.getId());
            userStatus.setNickname(player.getNickname());
            userStatus.setConnected(game.getWebsocketLobbyRegistry().contains(player.getId()));
            connectedPlayers.add(userStatus);
        }
        return connectedPlayers;
    }
}
