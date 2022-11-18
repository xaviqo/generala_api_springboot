package tech.xavi.generalabe.dto.lobby;

import lombok.Builder;
import lombok.Data;
import tech.xavi.generalabe.dto.auth.TokenPayload;

@Data
@Builder
public class LocalStorageSessionDto {

    private String nickname;
    private String password;
    private String lobbyId;
    private String playerId;
    private TokenPayload tokenPayload;

}
