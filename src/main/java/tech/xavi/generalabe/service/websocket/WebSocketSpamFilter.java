package tech.xavi.generalabe.service.websocket;

import lombok.Data;
import tech.xavi.generalabe.constant.Global;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
@Data
public class WebSocketSpamFilter {

    private Map<String, LocalDateTime> timeFromLastMsg;
    private Map<String,String> lastSentMsg;
    private Map<String,Integer> spamPoints;


    public WebSocketSpamFilter() {
        this.timeFromLastMsg = new HashMap<>();
        this.lastSentMsg = new HashMap<>();
        this.spamPoints = new HashMap<>();
    }

    public boolean containsUser(String userId){
        return timeFromLastMsg.containsKey(userId);
    }

    public boolean enoughTimeBetweenMsg(String userId, LocalDateTime newMsgTime){
        LocalDateTime lastMsgTime = timeFromLastMsg.get(userId);
        if (lastMsgTime != null){
            long diff = SECONDS.between(lastMsgTime,newMsgTime);
            if (diff<= Global.SEC_BTW_CHAT_MSG){
                increaseSpamPoints(userId);
                return false;
            }
            timeFromLastMsg.put(userId,newMsgTime);
        }
        return true;
    }

    public boolean isMessageDifferentFromPreviousOne(String userId, String newMessage){
        if (!newMessage.isBlank()){
            if (!lastSentMsg.get(userId)
                    .equalsIgnoreCase(newMessage)){
                lastSentMsg.put(userId,newMessage);
                return true;
            }
        }
        increaseSpamPoints(userId);
        return false;
    }

    public int getUserSpamPoints(String userId){
        return spamPoints.get(userId);
    }

    private void increaseSpamPoints(String userId){
        spamPoints.put(userId,spamPoints.get(userId)+1);
    }

    public void registerNewUser(String userId){
        timeFromLastMsg.put(userId, LocalDateTime.now().minusHours(1));
        lastSentMsg.put(userId, UUID.randomUUID().toString());
        spamPoints.put(userId, 0);
    }

    public void removeUser(String userId){
        timeFromLastMsg.remove(userId);
        lastSentMsg.remove(userId);
        spamPoints.remove(userId);
    }

    public boolean lobbyHasPlayers(){
        return (timeFromLastMsg.size() > 0);
    }

    
}
