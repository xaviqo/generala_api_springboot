package tech.xavi.generalabe.controller.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xavi.generalabe.dto.game.GameRulesDto;
import tech.xavi.generalabe.service.game.GameRulesService;

@AllArgsConstructor
@RestController()
@RequestMapping(path = "/api/game")
public class GameCfgRestController {

    private final GameRulesService gameRulesService;

    //RETURN ONLY OK!!!
    @PutMapping("cfg/{lobbyId}")
    public ResponseEntity<?> configureGameRules(@PathVariable String lobbyId, @RequestBody GameRulesDto dto){
        return new ResponseEntity<>(gameRulesService.configureGame(lobbyId,dto), HttpStatus.OK);
    }

    @GetMapping("cfg/{lobbyId}")
    public ResponseEntity<?> getGameRules(@PathVariable String lobbyId){
        return new ResponseEntity<>(gameRulesService.getGameRules(lobbyId), HttpStatus.OK);
    }

}
