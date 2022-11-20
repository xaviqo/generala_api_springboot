package tech.xavi.generalabe.controller.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.dto.websocket.LobbyMessageDto;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.model.LobbyInteraction;
import tech.xavi.generalabe.service.websocket.WebSocketChatService;
import tech.xavi.generalabe.service.websocket.WebSocketConnectionService;
import tech.xavi.generalabe.utils.mapper.TimeMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Controller
@Slf4j
public class ChatWsController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final WebSocketChatService webSocketChatService;
    private final WebSocketConnectionService webSocketConnectionService;

    @MessageMapping("/chat/{lobby}")
    public void lobbyChatRoom(@DestinationVariable String lobby, LobbyMessageDto message) {

        if (webSocketConnectionService.playerIsInLobbyRegistry(message.getSenderId(),lobby)){

            simpMessagingTemplate.convertAndSend(
                    Global.WS_LOBBY_TOPIC+lobby,
                    messageFilter(lobby,message)
            );

        } else {
            log.error("WS: BLOCKED UNAUTHORIZED CHAT MESSAGE FROM ID "+message.getSenderId()+" @ LOOBY "+lobby);
        }

    }

    private LobbyMessageDto messageFilter(String lobby, LobbyMessageDto message){

        boolean spam = false;

        //CHECK FIRST IF USER IS CHAT BANNED
        if (webSocketChatService.userHasReachedSpamPointsLimit(lobby,message.getSenderId())){
            spam = true;
            message.setInteraction(LobbyInteraction.SPAM);
            message.setMessage(
                    GeneralaError.SpamFilterNoSpamPoints.getCode()+" "+
                            GeneralaError.SpamFilterNoSpamPoints.getDescription()
            );
        }

        if (!spam &&
                !webSocketChatService.isMessageTooLong(message.getMessage())){
            spam = true;
            message.setInteraction(LobbyInteraction.SPAM);
            message.setMessage(
                    GeneralaError.SpamFilterMaxCharsExceeded.getCode()+" "+
                            GeneralaError.SpamFilterMaxCharsExceeded.getDescription()
            );
        }

        if (!spam &&
                !webSocketChatService.enoughTimeBetweenMsg(lobby,message.getSenderId(), LocalDateTime.now())
        ){
            spam = true;
            message.setMessage(
                    GeneralaError.SpamFilterTimeBetweenMsg.getCode()+" "+
                            GeneralaError.SpamFilterTimeBetweenMsg.getDescription()
            );
        }

        if (!spam &&
                !webSocketChatService.isMessageDifferentFromPreviousOne(lobby,message.getSenderId(), message.getMessage())){
            spam = true;
            message.setMessage(
                    GeneralaError.SpamFilterRepeatedMsg.getCode()+" "+
                            GeneralaError.SpamFilterRepeatedMsg.getDescription()
            );
        }

        if (spam){
            message.setInteraction(LobbyInteraction.SPAM);
            log.warn("WS: SPAM FILTER TRIGGERED FROM ID "+message.getSenderId()+" @ LOOBY "+lobby+ " | "+message);
        } else {
            message.setInteraction(LobbyInteraction.MESSAGE);
        }

        message.setTime(TimeMapper.stringifyLTN(LocalTime.now()));

        return message;
    }

}
