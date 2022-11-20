package tech.xavi.generalabe.controller.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tech.xavi.generalabe.model.LobbyInteraction;
import tech.xavi.generalabe.service.websocket.WebSocketChatService;
import tech.xavi.generalabe.service.websocket.WebSocketConnectionService;

@Slf4j
@AllArgsConstructor
@Component
public class ConnectionsWsController {

    private final WebSocketConnectionService webSocketConnectionService;
    private final WebSocketChatService webSocketChatService;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        final String userId = accessor.getNativeHeader("senderId").get(0);
        final String lobbyId = accessor.getNativeHeader("lobby").get(0);
        final String token = accessor.getNativeHeader("token").get(0);
        final String nickname = accessor.getNativeHeader("senderNickname").get(0);

        if (webSocketConnectionService.playerIsInLobby(userId,lobbyId,token)){
            webSocketChatService.saveWsSpamFilter(userId,lobbyId);
            webSocketConnectionService.saveWsLobbyRegistry(userId,lobbyId);
            webSocketConnectionService.sendActivityMessage(lobbyId,nickname,userId,LobbyInteraction.JOIN);
        } else {
            log.error("INVALID WS CONNECT CREDENTIALS FOR "+nickname+" @ LOBBY "+lobbyId);
        }

    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        String[] longSessionId = event.getSessionId().split(":");
        final String lobbyId = longSessionId[0];
        final String userId = longSessionId[1];
        final String userNickname = longSessionId[2];

        // DO NOT REMOVE USER FROM SPAM FILTER, TO AVOID RECONNECTIONS FOR SPAMMING
        //generalaLobbyService.removeWsSpamFilter(nickname,lobbyId);

        webSocketConnectionService.removeWsLobbyRegistry(userId,lobbyId);
        webSocketConnectionService.sendActivityMessage(lobbyId,userNickname,userId,LobbyInteraction.LEAVE);

    }




}
