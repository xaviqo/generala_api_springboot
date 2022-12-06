package tech.xavi.generalabe.service.game;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.configuration.security.jwt.JwtHelper;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.dto.websocket.GameMessageDto;
import tech.xavi.generalabe.dto.websocket.LobbyMessageDto;
import tech.xavi.generalabe.model.GameAction;
import tech.xavi.generalabe.service.user.CommonUserService;
import tech.xavi.generalabe.utils.game.GameTimer;
import tech.xavi.generalabe.utils.mapper.TimeMapper;

import java.time.LocalTime;

@Slf4j
@Service
@AllArgsConstructor
public class TimerService{

    private final JwtHelper jwtHelper;
    private final CommonGameService commonGameService;
    private final CommonUserService commonUserService;
    private static SimpMessagingTemplate simpMessagingTemplate;
    
    public static void timerMessage(String lobbyId, String timerAction){
        final String SEND_LOCATION = Global.WS_GAME_TOPIC+lobbyId;
        simpMessagingTemplate.convertAndSend(SEND_LOCATION,
                GameMessageDto.builder()
                        .gameAction(GameAction.TIMER)
                        .status(timerAction)
                        .build()
        );
    }


    // return true when all players are ready
    public boolean syncTimersFirstRound(String userId, String token){
        if (jwtHelper.getUserIdFromAccessToken(token).equals(userId)){

            final GeneralaUser generalaUser = commonUserService.findByUserId(userId);
            final Game game = commonGameService.findGameByLobbyId(generalaUser.getLobby());

            if (game.isJustStarted() && !game.getGameTimer().isReady()){

                game.getGameTimer().playerConnected(generalaUser.getId());
                commonGameService.updateGame(game);
                log.info("TIMER "+game.getLobbyId()+" IS READY ? "+game.getGameTimer().isReady());
                return game.getGameTimer().isReady();

            }
        }
        return false;
    }

    public void playerDisconnected(String userId){

        final GeneralaUser generalaUser = commonUserService.findByUserId(userId);
        final Game game = commonGameService.findGameByLobbyId(generalaUser.getLobby());
        final GameTimer gameTimer = game.getGameTimer();

        if (gameTimer != null) gameTimer.playerDisconnected(generalaUser.getId());

    }

}
