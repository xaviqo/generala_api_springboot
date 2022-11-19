package tech.xavi.generalabe.dto.lobby;

import lombok.Data;

@Data
public class UserLobbyConnectionStatus {

    private String id;
    private String nickname;
    private boolean connected;

}
