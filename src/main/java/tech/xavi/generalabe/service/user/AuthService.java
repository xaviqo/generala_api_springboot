package tech.xavi.generalabe.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.configuration.security.jwt.JwtHelper;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.document.RefreshToken;
import tech.xavi.generalabe.dto.auth.TokenPayload;
import tech.xavi.generalabe.repository.RefreshTokenRepository;

@AllArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    private final CommonUserService commonUserService;
    private final JwtHelper jwtHelper;

    public TokenPayload setAuthentication(String id, String password){

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(id, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .owner(authentication.getName()) //TODO: CHECK IF GETS NAME OR ID
                .build();

        refreshTokenRepository.save(refreshToken);

        return TokenPayload.builder()
                .accessToken(jwtHelper.generateAccessToken(id))
                .refreshToken(jwtHelper.generateRefreshToken(id,refreshToken))
                .build();
    }

    public TokenPayload renewAccessToken(TokenPayload dto){

        String refreshTokenId = jwtHelper.getTokenIdFromRefreshToken(dto.getRefreshToken());

        if (checkRefreshToken(dto.getRefreshToken(), refreshTokenId)) {

            GeneralaUser user = commonUserService.findByUserId(jwtHelper.getUserIdFromRefreshToken(dto.getRefreshToken()));
            String accessToken = jwtHelper.generateAccessToken(user.getId());

            return TokenPayload.builder()
                    .accessToken(accessToken)
                    .refreshToken(dto.getRefreshToken())
                    .build();
        }
        return null;
    }

    private boolean checkRefreshToken(String refreshToken, String refreshTokenId){
        return (jwtHelper.validateRefreshToken(refreshToken) &&
                refreshTokenRepository.existsById(refreshTokenId));
    }
}
