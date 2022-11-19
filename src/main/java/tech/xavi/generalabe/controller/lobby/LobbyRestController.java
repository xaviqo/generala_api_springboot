package tech.xavi.generalabe.controller.lobby;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xavi.generalabe.service.game.LobbyService;
import tech.xavi.generalabe.service.game.MatchingService;

@AllArgsConstructor
@RestController()
@RequestMapping(path = "/api/lobby")
public class LobbyRestController {

    private final MatchingService matchingService;
    private final LobbyService lobbyService;

    @PostMapping("new/{nickname}")
    public ResponseEntity<?> createNewLobby(@PathVariable String nickname){
        return new ResponseEntity<>(matchingService.prepareGameAndAdmin(nickname), HttpStatus.CREATED);
    }

    @PostMapping("join/{lobbyId}/{nickname}")
    public ResponseEntity<?> joinLobby(@PathVariable String lobbyId, @PathVariable String nickname){
        return new ResponseEntity<>(matchingService.joinGameAndPrepareUser(lobbyId,nickname), HttpStatus.CREATED);
    }

    @GetMapping("users")
    public ResponseEntity<?> joinLobby(){
        return new ResponseEntity<>(lobbyService.usersInLobby(), HttpStatus.OK);
    }



}
