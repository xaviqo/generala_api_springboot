package tech.xavi.generalabe.controller.game;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.xavi.generalabe.service.game.TurnService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/turn")
public class TurnController {

    private final TurnService turnService;

    @GetMapping
    public ResponseEntity<?> getTurn(){
        return new ResponseEntity<>(turnService.getTurn(), HttpStatus.OK);
    }
}
