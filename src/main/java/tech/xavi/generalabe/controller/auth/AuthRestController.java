package tech.xavi.generalabe.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.xavi.generalabe.dto.auth.TokenPayload;
import tech.xavi.generalabe.exception.GeneralaError;
import tech.xavi.generalabe.service.user.AuthService;

@AllArgsConstructor
@RestController()
@RequestMapping(path = "/api/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("new/access-token")
    public ResponseEntity<?> renewAccessToken(@RequestBody TokenPayload dto) {
        TokenPayload payload = authService.renewAccessToken(dto);
        if (payload != null) return ResponseEntity.ok(payload);
        throw new BadCredentialsException(GeneralaError.JWTVerificationRefreshToken.getDescription());
    }
}
