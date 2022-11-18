package tech.xavi.generalabe.constant;

public class Global {

    // PREFIX GLOBALS //

    public static final String PLAYER_PREFIX = "PL_";
    public static final String ADMIN_PREFIX = "AD_";
    public static final String LOBBY_PREFIX = "LB_";


    // SECURITY GLOBALS //

    public static final int ACCESS_TKN_EXP_MIN = 5;
    public static final int REFRESH_TKN_EXP_DAY = 30;

    // GAME GLOBALS //

    public static final int MAX_ROUNDS = 12;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    public static final int DICES_PER_PLAYER = 5;
    public static final int MAX_GAME_PWD_LENGTH = 12;
    public static final int MIN_GAME_PWD_LENGTH = 4;


    // CHAT GLOBALS //

    public static final int SEC_BTW_CHAT_MSG = 3;

    public static final int MAX_CHAT_SPAM_POINTS = 5;

    public static final int MAX_CHAT_MSG_LEN = 85;
}