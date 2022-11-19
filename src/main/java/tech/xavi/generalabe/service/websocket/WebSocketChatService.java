package tech.xavi.generalabe.service.websocket;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.xavi.generalabe.constant.Global;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Service
public class WebSocketChatService {

    //TODO: Remove spamfilter when game finishes
    private final Map<String, WebSocketSpamFilter> spamFilters;

    //TODO: IMPORTANT! CHECK IF SPAM_FILTER IS BEING SAVED

    public void saveWsSpamFilter(String userId, String lobbyId){
        WebSocketSpamFilter spamFilter = spamFilters.get(lobbyId);
        if (spamFilter == null){
            spamFilter = new WebSocketSpamFilter();
            spamFilters.put(lobbyId,spamFilter);
        }
        if (!spamFilter.containsUser(userId)) spamFilter.registerNewUser(userId);
    }

    public boolean userHasReachedSpamPointsLimit(String lobbyId, String userId){
        WebSocketSpamFilter spamFilter = retrieveSpamFilter(lobbyId);
        if (spamFilter == null) return false;
        return spamFilter.getUserSpamPoints(userId) >= Global.MAX_CHAT_SPAM_POINTS;
    }

    public boolean isMessageTooLong(String chatMessage){
        return (chatMessage.length() <= Global.MAX_CHAT_MSG_LEN);
    }

    public boolean enoughTimeBetweenMsg(String lobbyId, String userId, LocalDateTime newMsgTime){
        WebSocketSpamFilter spamFilter = retrieveSpamFilter(lobbyId);
        if (spamFilter == null) return false;
        return spamFilter.enoughTimeBetweenMsg(userId,newMsgTime);
    }

    public boolean isMessageDifferentFromPreviousOne(String lobbyId, String userId, String newMsg){
        WebSocketSpamFilter spamFilter = retrieveSpamFilter(lobbyId);
        if (spamFilter == null) return false;
        return spamFilter.isMessageDifferentFromPreviousOne(userId,newMsg);
    }


    private WebSocketSpamFilter retrieveSpamFilter(String lobbyId){
        return spamFilters.get(lobbyId);
    }

}
