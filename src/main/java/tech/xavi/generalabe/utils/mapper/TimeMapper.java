package tech.xavi.generalabe.utils.mapper;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeMapper {

    public static String stringifyLTN(LocalTime time){
        String h = String.valueOf(time.getHour());
        String m = (time.getMinute()<10) ? "0"+time.getMinute() : String.valueOf(time.getMinute());
        String s = String.valueOf(time.getSecond());
        return h+":"+m+":"+s;
    }

}
