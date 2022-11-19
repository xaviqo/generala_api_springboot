package tech.xavi.generalabe.dto.websocket;

import lombok.Builder;
import lombok.Data;
import tech.xavi.generalabe.model.LobbyInteraction;

@Data
@Builder
public class LobbyMessageDto {

    private LobbyInteraction interaction;
    private String message;
    private String senderId;
    private String senderNickname;
    private String time;

}