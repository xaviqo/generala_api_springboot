package tech.xavi.generalabe.utils;

import tech.xavi.generalabe.constant.Global;

import java.util.UUID;

public class UUIDGenerala {

    public static String random8Chars(){
        final String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.substring(uuid.length()-8).toUpperCase();
    }
}
