package tech.xavi.generalabe.utils.mapper;

import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.model.DiceCup;
import tech.xavi.generalabe.model.Player;
import tech.xavi.generalabe.model.ScoreSheet;

public class PlayerUserMapper {

    public static Player toPlayer(GeneralaUser generalaUser){
        return Player.builder()
                .id(generalaUser.getId())
                .nickname(generalaUser.getNickname())
                .ingameId(0)
                .scoreSheet(new ScoreSheet())
                .diceCup(new DiceCup())
                .build();
    }
}
