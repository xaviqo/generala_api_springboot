package tech.xavi.generalabe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CombinationCategory {

    //INFO: https://www.guiaspracticas.com/juegos-de-mesa/generala

    ACES("aces"),
    DEUCES("deuces"),
    THREES("threes"),
    FOURS("fours"),
    FIVES("fives"),
    SIXES("sixes"),
    SUBTOTAL("subtotal"),
    BONUS("bonus"),
    FREE("free"),
    FOUR_OF_A_KIND("4 of a kind"),
    FULL_HOUSE("full house"),
    SHORT_STRAIGHT("short straight"),
    LONG_STRAIGHT("long straight"),
    GENERALA("generala"),
    TOTAL("total");

    private final String category;



}