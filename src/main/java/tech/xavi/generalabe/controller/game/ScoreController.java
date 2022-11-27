package tech.xavi.generalabe.controller.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.xavi.generalabe.service.game.ScoreService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/score")
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping("/table")
    public ResponseEntity<?> configureGameRules(){
        return new ResponseEntity<>(scoreService.getGameScoreTable(), HttpStatus.OK);
    }
}
