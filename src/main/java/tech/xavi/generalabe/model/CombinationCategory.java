package tech.xavi.generalabe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CombinationCategory {

    //INFO: https://www.guiaspracticas.com/juegos-de-mesa/generala

    ONES("ones",null,null),
    TWOS("twos",null,null),
    THREES("threes",null,null),
    FOURS("fours",null,null),
    FIVES("fives",null,null),
    SIXES("sixes",null,null),
    FREE("free",null,null),
    SHORT_STRAIGHT("short",20,5),
    LONG_STRAIGHT("long",30,5),
    FULL_HOUSE("full",30,5),
    POKER("poker",40,5),
    GENERALA("generala",50,10);

    private final String combination;
    private final Integer points;
    private final Integer servedExtra;

}