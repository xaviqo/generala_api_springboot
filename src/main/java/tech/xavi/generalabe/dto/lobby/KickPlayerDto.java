package tech.xavi.generalabe.dto.lobby;

import lombok.Data;

@Data
public class KickPlayerDto {

    private String lobbyId;
    private String playerToKickId;

}
