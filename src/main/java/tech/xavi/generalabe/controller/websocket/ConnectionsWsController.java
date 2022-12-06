package tech.xavi.generalabe.controller.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.dto.websocket.GameMessageDto;
import tech.xavi.generalabe.model.GameAction;
import tech.xavi.generalabe.model.LobbyInteraction;
import tech.xavi.generalabe.service.game.TimerService;
import tech.xavi.generalabe.service.websocket.WebSocketChatService;
import tech.xavi.generalabe.service.websocket.WebSocketConnectionService;

import java.time.Duration;

@Slf4j
@AllArgsConstructor
@Component
public class ConnectionsWsController {

    private final WebSocketConnectionService webSocketConnectionService;
    private final WebSocketChatService webSocketChatService;
    private final TimerService timerService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        final String userId = accessor.getNativeHeader("senderId").get(0);
        final String topic = accessor.getNativeHeader("topic").get(0);
        final String lobbyId = accessor.getNativeHeader("lobby").get(0);
        final String token = accessor.getNativeHeader("token").get(0);
        final String nickname = accessor.getNativeHeader("senderNickname").get(0);

        if (webSocketConnectionService.playerIsInLobby(userId,lobbyId,token) && Global.CHAT_TOPIC.equals(topic)){
            webSocketChatService.saveWsSpamFilter(userId,lobbyId);
            webSocketConnectionService.saveWsLobbyRegistry(userId,lobbyId);
            webSocketConnectionService.sendActivityMessage(lobbyId,nickname,userId,LobbyInteraction.JOIN);
        }

        if (Global.GAME_TOPIC.equals(topic) && timerService.syncTimersFirstRound(userId,token)) {

            try {
                Thread.sleep(Duration.ofSeconds(1));
                simpMessagingTemplate.convertAndSend(Global.WS_GAME_TOPIC+lobbyId,
                        GameMessageDto.builder()
                                .gameAction(GameAction.INIT)
                                .build()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        String[] longSessionId = event.getSessionId().split(":");
        final String lobbyId = longSessionId[0];
        final String userId = longSessionId[1];
        final String userNickname = longSessionId[2];
        final String topic = longSessionId[3];

        // DO NOT REMOVE USER FROM SPAM FILTER, TO AVOID RECONNECTIONS FOR SPAMMING
        //generalaLobbyService.removeWsSpamFilter(nickname,lobbyId);

        if (!Global.CHAT_TOPIC.equals(topic)) timerService.playerDisconnected(userId);
        webSocketConnectionService.removeWsLobbyRegistry(userId,lobbyId);
        webSocketConnectionService.sendActivityMessage(lobbyId,userNickname,userId,LobbyInteraction.LEAVE);

    }




}
