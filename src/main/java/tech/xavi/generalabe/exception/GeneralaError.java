package tech.xavi.generalabe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tech.xavi.generalabe.constant.Global;

@AllArgsConstructor
@Getter
public enum GeneralaError {

    GeneralError("000","Generala Exception! This should not be happening..."),
    UserIdNotFound("001","User ID not found"),
    InvalidGameId("002","This lobby is not associated with your user"),
    InvalidAmountPlayers("003","Invalid amount of players"),
    InvalidPasswordLength("004","Password length is invalid"),
    GameIdNotFound("005","There is no available game with this id"),
    GameAlreadyConfigured("006","This game has already been configured"),
    GameStartedOrFinished("007","The game has already started/finished or has reached the limit of players"),
    InvalidGamePassword("008","Wrong game password"),
    AlreadyIngame("009","You cannot play more than one game at a time"),
    AlreadyJoined("010","You have already joined this game"),

    SpamFilterTimeBetweenMsg("011","You should leave more time between chat messages"),
    SpamFilterRepeatedMsg("012","Do not repeat the same message in the chat"),
    SpamFilterNoSpamPoints("013","You have been banned from the chat"),
    SpamFilterMaxCharsExceeded("014","You have exceeded the limit of characters ("+ Global.MAX_CHAT_MSG_LEN +") per message"),

    JWTVerificationAccessToken("015","Invalid access token"),
    JWTVerificationRefreshToken("016","Invalid refresh token"),

    NotGameAdmin("017","Only the game administrator can do this action"),
    NotEnoughPlayers("018","At least two ready players are needed to start the game"),
    NicknameOnlyAZ09("019","Nicknames can only contain letters or numbers"),
    NicknameMaxLength("020","Nicknames maximum length is "+Global.NICKNAME_MAX_LEN+" characters."),
    NicknameMinLength("021","Nicknames minimum length is "+Global.NICKNAME_MIN_LEN+" characters.")
    ;

    private final String code;
    private final String description;
}
