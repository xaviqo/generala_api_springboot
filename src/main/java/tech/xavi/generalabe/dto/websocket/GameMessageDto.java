package tech.xavi.generalabe.dto.websocket;

import lombok.Builder;
import lombok.Data;
import tech.xavi.generalabe.model.GameAction;

@Data
@Builder
public class GameMessageDto {
    private GameAction gameAction;
    private String status;
}
