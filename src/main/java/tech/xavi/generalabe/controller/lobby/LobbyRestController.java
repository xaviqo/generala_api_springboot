package tech.xavi.generalabe.controller.lobby;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xavi.generalabe.dto.GameRulesDto;
import tech.xavi.generalabe.dto.lobby.JoinLobbyDto;
import tech.xavi.generalabe.service.game.InitGameService;
import tech.xavi.generalabe.service.game.MatchingService;

@AllArgsConstructor
@RestController()
@RequestMapping(path = "/api/lobby")
public class LobbyRestController {

    private final MatchingService matchingService;

    @PostMapping("new/{nickname}")
    public ResponseEntity<?> createNewLobby(@PathVariable String nickname){
        return new ResponseEntity<>(matchingService.prepareGameAndAdmin(nickname), HttpStatus.CREATED);
    }

    public ResponseEntity<?> joinLobby(@RequestBody JoinLobbyDto dto){
        return new ResponseEntity<>(matchingService.joinGameAndPrepareUser(dto), HttpStatus.CREATED);
    }



}
