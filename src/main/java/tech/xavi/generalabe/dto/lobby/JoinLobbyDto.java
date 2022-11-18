package tech.xavi.generalabe.dto.lobby;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinLobbyDto {

    private String nickname;
    private String lobbyId;

}
