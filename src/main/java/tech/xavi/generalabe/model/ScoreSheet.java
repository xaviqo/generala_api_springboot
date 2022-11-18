package tech.xavi.generalabe.model;

import lombok.Data;

@Data
public class ScoreSheet {

    private int ones;
    private int twos;
    private int threes;
    private int fours;
    private int fives;
    private int sixes;
    private int free;
    private int shortStraight;
    private int longStraight;
    private int fullHouse;
    private int poker;
    private int generala;
    private int totalScore;

}