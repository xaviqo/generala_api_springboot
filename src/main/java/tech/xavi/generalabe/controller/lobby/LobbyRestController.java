package tech.xavi.generalabe.controller.lobby;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.xavi.generalabe.dto.lobby.KickPlayerDto;
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

    @PostMapping("join/{lobbyId}/{nickname}")
    public ResponseEntity<?> joinLobby(@PathVariable String lobbyId, @PathVariable String nickname){
        return new ResponseEntity<>(matchingService.joinGameAndPrepareUser(lobbyId,nickname), HttpStatus.CREATED);
    }

    @PostMapping("leave/{lobbyId}/{nickname}")
    public ResponseEntity<?> leaveLobby(@PathVariable String lobbyId, @PathVariable String nickname){
        matchingService.leaveLobby(lobbyId,nickname);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("start")
    public ResponseEntity<?> startLobby(){
        matchingService.startGame();
        return ResponseEntity.ok("OK");
    }

    @PostMapping("kick")
    public ResponseEntity<?> leaveLobby(@RequestBody KickPlayerDto dto){
        matchingService.kickPlayer(dto);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteLobby(){
        matchingService.deleteLobby();
        return ResponseEntity.ok("OK");
    }


}
