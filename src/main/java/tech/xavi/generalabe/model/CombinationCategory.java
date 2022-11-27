package tech.xavi.generalabe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CombinationCategory {

    //INFO: https://www.guiaspracticas.com/juegos-de-mesa/generala

    ACES("aces",1),
    DEUCES("deuces",2),
    THREES("threes",3),
    FOURS("fours",4),
    FIVES("fives",5),
    SIXES("sixes",6),
    FREE("free",9),
    FOUR_OF_A_KIND("4 of a kind",10),
    FULL_HOUSE("full house",11),
    SHORT_STRAIGHT("short straight",12),
    LONG_STRAIGHT("long straight",13),
    GENERALA("generala",14),

    BONUS("bonus",8),
    SUBTOTAL("subtotal",7),
    TOTAL("total",15);

    private final String category;
    private final int sort;

}