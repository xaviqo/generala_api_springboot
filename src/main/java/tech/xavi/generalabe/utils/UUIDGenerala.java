package tech.xavi.generalabe.utils;

import tech.xavi.generalabe.constant.Global;

import java.util.UUID;

public class UUIDGenerala {

    public static String generatePlayerId(String nickname){
        return Global.PLAYER_PREFIX+random8Chars()+"_"+nickname.toUpperCase();
    }

    public static String generateAdminId(String nickname){
        return Global.ADMIN_PREFIX+random8Chars()+"_"+nickname.toUpperCase();
    }

    public static String generateLobbyId(){
        return Global.LOBBY_PREFIX+random8Chars();
    }

    public static String random8Chars(){
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(uuid.length()-8).toUpperCase();
    }
}
