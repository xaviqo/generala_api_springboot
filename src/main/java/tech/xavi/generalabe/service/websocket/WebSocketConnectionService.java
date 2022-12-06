package tech.xavi.generalabe.service.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.configuration.security.jwt.JwtHelper;
import tech.xavi.generalabe.constant.Global;
import tech.xavi.generalabe.document.Game;
import tech.xavi.generalabe.dto.websocket.LobbyMessageDto;
import tech.xavi.generalabe.model.LobbyInteraction;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.service.game.CommonGameService;
import tech.xavi.generalabe.utils.mapper.TimeMapper;

import java.time.LocalTime;

@AllArgsConstructor
@Slf4j
@Service
public class WebSocketConnectionService {

    private final JwtHelper jwtHelper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CommonGameService commonGameService;

    public boolean playerIsInLobby(String userId, String lobbyId, String tkn){
        if (jwtHelper.getUserIdFromAccessToken(tkn).equals(userId)){
            Game game = commonGameService.findGameByLobbyId(lobbyId);
            for (Player p:  game.getPlayers())
                if (p.getId().equals(userId)) return true;
        }
        return false;
    }

    public boolean playerIsInLobbyRegistry(String userId, String lobbyId){
        Game game = commonGameService.findGameByLobbyId(lobbyId);
        for (String id:  game.getWebsocketLobbyRegistry())
            if (id.equals(userId)) return true;
        return false;
    }

    public void removeWsLobbyRegistry(String userId, String lobbyId){
        Game game = commonGameService.findGameByLobbyId(lobbyId);
        game.removeWebsocketLobbyRegistry(userId);
        commonGameService.updateGame(game);
    }

    public void saveWsLobbyRegistry(String userId, String lobbyId){
        Game game = commonGameService.findGameByLobbyId(lobbyId);
        game.setWebsocketLobbyRegistry(userId);
        commonGameService.updateGame(game);
    }

    public void sendActivityMessage(String lobbyId, String nickname, String userId, LobbyInteraction interaction){

        final String SEND_LOCATION = Global.WS_LOBBY_TOPIC+lobbyId;

        String message = switch (interaction) {
            case JOIN -> nickname + Global.WS_CONNECT_MSG;
            case LEAVE -> nickname + Global.WS_DISCONNECT_MGS;
            case KICK -> nickname + Global.WS_KICK_MSG;
            case DELETE -> Global.WS_DELETE_MSG;
            default -> "Init countdown";
        };

        log.info("WS: lobby-id: "+lobbyId+" "+message+" "+interaction.name());

        if (interaction != LobbyInteraction.START){
            simpMessagingTemplate.convertAndSend(SEND_LOCATION,
                    LobbyMessageDto.builder()
                            .interaction(interaction)
                            .message(message)
                            .senderId(userId)
                            .senderNickname(nickname)
                            .time(TimeMapper.stringifyLTN(LocalTime.now()))
                            .build()
            );
        }

        if (interaction == LobbyInteraction.START){
            simpMessagingTemplate.convertAndSend(SEND_LOCATION,
                    LobbyMessageDto.builder()
                            .interaction(LobbyInteraction.START)
                            .message(Global.WS_START_MSG)
                            .senderId("count_")
                            .senderNickname("")
                            .time("")
                            .build()
            );

            for (int i = 5; i >= 0; i--) {
                simpMessagingTemplate.convertAndSend(SEND_LOCATION,
                        LobbyMessageDto.builder()
                                .interaction(LobbyInteraction.START)
                                .message("Countdown... "+i)
                                .senderId("count_"+i)
                                .senderNickname("")
                                .time("")
                                .build()
                );
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("WS: lobby-id: "+lobbyId+" Finished countdown "+LobbyInteraction.START.name());

        }

    }


}
