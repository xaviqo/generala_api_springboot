package tech.xavi.generalabe.service.websocket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.configuration.security.jwt.JwtHelper;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.service.game.CommonGameService;

@AllArgsConstructor
@Service
public class WebSocketConnectionService {

    private final JwtHelper jwtHelper;
    private final CommonGameService commonGameService;

    public boolean playerIsInLobby(String userId, String lobbyId, String tkn){
        if (jwtHelper.getUserIdFromAccessToken(tkn).equals(userId)){
            Game game = commonGameService.findGameByLobbyId(lobbyId);
            for (Player p:  game.getPlayers())
                if (p.getId().equals(userId)) return true;
        }
        return false;
    }

    public boolean playerIsInLobbyRegistry(String userId, String lobbyId){
        Game game = commonGameService.findGameByLobbyId(lobbyId);
        for (String id:  game.getWebsocketLobbyRegistry())
            if (id.equals(userId)) return true;
        return false;
    }

    public void removeWsLobbyRegistry(String userId, String lobbyId){
        Game game = commonGameService.findGameByLobbyId(lobbyId);
        game.removeWebsocketLobbyRegistry(userId);
        commonGameService.updateGame(game);
    }

    public void saveWsLobbyRegistry(String userId, String lobbyId){
        Game game = commonGameService.findGameByLobbyId(lobbyId);
        game.setWebsocketLobbyRegistry(userId);
        commonGameService.updateGame(game);
    }


}
