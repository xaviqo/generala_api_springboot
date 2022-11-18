package tech.xavi.generalabe.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPayload {

    String accessToken;
    String refreshToken;

}
