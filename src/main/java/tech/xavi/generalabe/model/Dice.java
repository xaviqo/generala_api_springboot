package tech.xavi.generalabe.model;

import lombok.Data;

import java.util.Random;

@Data
public class Dice {

    private int id;
    private int value;

    public Dice(int id) {
        this.id = id;
        this.value = 0;
    }

    public void rollDice(){
        this.value = new Random().nextInt(7)+1;
    }

}