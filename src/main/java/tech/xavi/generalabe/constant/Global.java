package tech.xavi.generalabe.constant;

public class Global {


    // SECURITY GLOBALS //

    public static final int ACCESS_TKN_EXP_MIN = 5;
    public static final int REFRESH_TKN_EXP_DAY = 30;
    public static final String REGEX_LETTERS_NUMBERS = "[a-zA-Z0-9]*";

    // GAME GLOBALS //

    public static final int MAX_ROUNDS = 12;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    public static final int DICES_PER_PLAYER = 5;
    public static final int MAX_GAME_PWD_LENGTH = 12;
    public static final int MIN_GAME_PWD_LENGTH = 4;
    public static final int NICKNAME_MAX_LEN = 14;
    public static final int NICKNAME_MIN_LEN = 4;

    // WS TOPICS //

    public static final String CHAT_TOPIC = "chat";
    public static final String GAME_TOPIC = "game" ;


    // CHAT GLOBALS //

    public static final int SEC_BTW_CHAT_MSG = 3;
    public static final int MAX_CHAT_SPAM_POINTS = 8;
    public static final int MAX_CHAT_MSG_LEN = 85;
    public static final String WS_LOBBY_TOPIC = "/topic/messages/";
    public static final String WS_GAME_TOPIC = "/topic/game/";
    public static final String WS_CONNECT_MSG = " just connected to the room";
    public static final String WS_DISCONNECT_MGS = " just disconnected from the room";
    public static final String WS_KICK_MSG = " kicked from the room";
    public static final String WS_DELETE_MSG = "The room has been deleted by the administrator";
    public static final String WS_START_MSG = "The game is about to begin!";


}