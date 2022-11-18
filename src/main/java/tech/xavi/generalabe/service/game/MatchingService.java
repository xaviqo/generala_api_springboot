package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.auth.TokenPayload;
import tech.xavi.generalabe.dto.lobby.JoinLobbyDto;
import tech.xavi.generalabe.dto.lobby.LocalStorageSessionDto;
import tech.xavi.generalabe.service.user.AuthService;
import tech.xavi.generalabe.service.user.CommonUserService;
import tech.xavi.generalabe.service.user.UserService;
import tech.xavi.generalabe.utils.UUIDGenerala;

@AllArgsConstructor
@Service
public class MatchingService {

    private final UserService userService;
    private final CommonUserService commonUserService;
    private final AuthService authService;

    private final InitGameService initGameService;

    public LocalStorageSessionDto prepareGameAndAdmin(String nickname){

        String password = UUIDGenerala.random8Chars();

        GeneralaUser admin = userService.createUserAndSave(nickname,password,true);
        admin.setLobby(UUIDGenerala.generateLobbyId());
        commonUserService.updateUserData(admin);

        TokenPayload tokenPayload = authService.setAuthentication(admin.getId(), password);

        initGameService.initGame(admin);

        return LocalStorageSessionDto.builder()
                .nickname(admin.getNickname())
                .lobbyId(admin.getLobby())
                .password(password)
                .playerId(admin.getId())
                .tokenPayload(tokenPayload)
                .build();

    }

    public LocalStorageSessionDto joinGameAndPrepareUser(JoinLobbyDto dto){

        //check lobby configured but game not started&&finished

        String password = UUIDGenerala.random8Chars();

        GeneralaUser user = userService.createUserAndSave(dto.getNickname(),password,false);
        user.setLobby(dto.getLobbyId());
        commonUserService.updateUserData(user);

        TokenPayload tokenPayload = authService.setAuthentication(user.getId(), user.getPassword());

        return LocalStorageSessionDto.builder()
                .nickname(user.getNickname())
                .lobbyId(user.getLobby())
                .password(password)
                .playerId(user.getId())
                .tokenPayload(tokenPayload)
                .build();
    }


}
