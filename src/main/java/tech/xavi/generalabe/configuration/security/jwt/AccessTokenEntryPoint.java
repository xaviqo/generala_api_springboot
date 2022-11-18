package tech.xavi.generalabe.configuration.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class AccessTokenEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn(authException.getMessage()+" @ "+ LocalDateTime.now());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "PLACE_HOLDER_CHANGE_FOR_INVALID_CREDENTIALS");
    }
}