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
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.websocket.LobbyMessageDto;
import tech.xavi.generalabe.model.LobbyInteraction;
import tech.xavi.generalabe.service.user.CommonUserService;
import tech.xavi.generalabe.service.websocket.WebSocketChatService;
import tech.xavi.generalabe.service.websocket.WebSocketConnectionService;
import tech.xavi.generalabe.utils.mapper.TimeMapper;

import java.time.LocalTime;

@Slf4j
@AllArgsConstructor
@Component
public class ConnectionsWsController {

    private final CommonUserService commonUserService;
    private final WebSocketConnectionService webSocketConnectionService;
    private final WebSocketChatService webSocketChatService;
    private final SimpMessagingTemplate simpMessagingTemplate;


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
            sendActivityMessage(lobbyId,nickname,userId,LobbyInteraction.JOIN);
        } else {
            log.error("INVALID WS CONNECT CREDENTIALS FOR "+nickname+" @ LOBBY "+lobbyId);
        }

    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        String[] longSessionId = event.getSessionId().split(":");
        final String lobbyId = longSessionId[0];
        final String userId = longSessionId[1];

        GeneralaUser user = commonUserService.findByUserId(userId);

        // DO NOT REMOVE USER FROM SPAM FILTER, TO AVOID RECONNECTIONS FOR SPAMMING
        //generalaLobbyService.removeWsSpamFilter(nickname,lobbyId);

        webSocketConnectionService.removeWsLobbyRegistry(userId,lobbyId);
        sendActivityMessage(lobbyId,user.getNickname(),userId,LobbyInteraction.LEAVE);

    }


    private void sendActivityMessage(String lobbyId, String nickname, String userId, LobbyInteraction interaction){

        String message = switch (interaction) {
            case JOIN -> nickname + Global.WS_CONNECT_MSG;
            case LEAVE -> nickname + Global.WS_DISCONNECT_MGS;
            default -> "";
        };

        log.info("WS: lobby-id: "+lobbyId+" "+message);

        simpMessagingTemplate.convertAndSend("/topic/messages/"+lobbyId,
                LobbyMessageDto.builder()
                        .interaction(interaction)
                        .message(message)
                        .senderId(userId)
                        .senderNickname(nickname)
                        .time(TimeMapper.stringifyLTN(LocalTime.now()))
                        .build()
        );
    }

}
