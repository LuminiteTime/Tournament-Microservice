package university.innopolis.tabletennis.tournamentmicroservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Brackets;
import university.innopolis.tabletennis.tournamentmicroservice.entity.BracketsMatch;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.service.BracketsService;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/brackets")
public class BracketsController {

    private final BracketsService bracketsService;

    @PostMapping
    public ResponseEntity<Brackets> createBrackets(@RequestBody List<PlayerDTO> players) {
        log.info("Creating brackets with players: {}", players);
        return ResponseEntity.ok().body(bracketsService.createBrackets(players.stream().map(MappingUtils::mapToPlayerEntity).toList()));
    }

    @PatchMapping("/{bracketsId}/")
    public ResponseEntity<Brackets> finishBrackets(@PathVariable Long bracketsId) {
        log.info("Completing brackets with id: {}", bracketsId);
        return ResponseEntity.ok().body(bracketsService.finishBrackets(bracketsId));
    }

    @PatchMapping("/{bracketsId}/match/{matchIndex}")
    public ResponseEntity<BracketsMatch> patchBracketsMatchState(@PathVariable Long bracketsId,
                                                                 @PathVariable Long matchIndex,
                                                                 @RequestBody Optional<PatchMatchDTO> matchInfo) {
        log.info("Patching match with id {}", matchIndex);
        return ResponseEntity.ok().body(bracketsService.patchBracketsMatchState(
                bracketsId,
                matchIndex,
                matchInfo));
    }

    @GetMapping("/{bracketsId}")
    public ResponseEntity<List<BracketsMatch>> getAllMatches(@PathVariable Long bracketsId) {
        log.info("Retrieving all matches");
        return ResponseEntity.ok().body(bracketsService.getAllMatches(bracketsId));
    }

    @GetMapping("/{bracketsId}/available_matches")
    public ResponseEntity<Set<BracketsMatch>> getAvailableMatches(@PathVariable Long bracketsId) {
        log.info("Retrieving available matches");
        return ResponseEntity.ok().body(bracketsService.getAvailableMatches(bracketsId));
    }
}
