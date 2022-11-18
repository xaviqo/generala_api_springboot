package tech.xavi.generalabe.controller.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xavi.generalabe.dto.GameRulesDto;
import tech.xavi.generalabe.service.game.InitGameService;

@AllArgsConstructor
@RestController()
@RequestMapping(path = "/api/game")
public class GameCfgRestController {

    private final InitGameService initGameService;

    @PutMapping("cfg")
    public ResponseEntity<?> configureGameRules(@RequestBody GameRulesDto dto){
        return new ResponseEntity<>(initGameService.configureGame(dto), HttpStatus.OK);
    }

}
