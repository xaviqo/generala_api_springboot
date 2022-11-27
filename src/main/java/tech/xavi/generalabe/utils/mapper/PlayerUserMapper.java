package tech.xavi.generalabe.utils.mapper;

import tech.xavi.generalabe.document.GeneralaUser;
import tech.xavi.generalabe.model.DiceCup;
import tech.xavi.generalabe.model.Player;

import java.util.HashMap;

public class PlayerUserMapper {

    public static Player toPlayer(GeneralaUser generalaUser){
        return Player.builder()
                .id(generalaUser.getId())
                .nickname(generalaUser.getNickname())
                .ingameId(0)
                .scoreSheet(new HashMap<>())
                .diceCup(new DiceCup())
                .build();
    }
}
