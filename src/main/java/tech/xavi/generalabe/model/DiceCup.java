package tech.xavi.generalabe.model;

import lombok.Data;
import tech.xavi.generalabe.constant.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class DiceCup {
    List<Dice> dices;

    public DiceCup() {
        this.dices = generatePlayerDices();
    }

    public List<Dice> roll(List<Integer> dicesIds){

        Optional.ofNullable(dicesIds).ifPresentOrElse(dIds -> {
                    this.dices.forEach( d -> {
                        if (dIds.contains(d.getId())) d.rollDice();
                    });
                },
                () -> this.dices.forEach(Dice::rollDice));

        return this.dices;

    }

    private List<Dice> generatePlayerDices(){
        List<Dice> dices = new ArrayList<>();
        for (int i = 0; i < Global.DICES_PER_PLAYER; i++) {
            dices.add(new Dice(i));
        }
        return dices;
    }

}
