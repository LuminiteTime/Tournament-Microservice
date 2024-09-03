package university.innopolis.tabletennis.tournamentmicroservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.dto.BracketsMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.WinnerBrackets;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.service.BracketsService;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("tournaments/brackets") // TODO: Change path in swagger (/tournaments/{tournamentId}/brackets).
public class BracketsController {

    private final BracketsService bracketsService;

    @PostMapping
    public ResponseEntity<WinnerBrackets> createBrackets(@RequestBody List<PlayerDTO> players) {
        log.info("Creating brackets with players: {}", players);
        return ResponseEntity.ok().body(bracketsService.createBrackets(players.stream().map(MappingUtils::mapToPlayerEntity).toList()));
    }

    @PatchMapping("/{bracketsId}/")
    public ResponseEntity<WinnerBrackets> finishBrackets(@PathVariable Long bracketsId) {
        log.info("Completing brackets with id: {}", bracketsId);
        return ResponseEntity.ok().body(bracketsService.finishBrackets(bracketsId));
    }

    @PatchMapping("/{bracketsId}/match/{matchIndex}") // TODO: /match/{matchIndex}, get match from tournament matches.
    public ResponseEntity<WinnerBracketsMatch> patchBracketsMatchState(@PathVariable Long bracketsId,
                                                                       @PathVariable Long matchIndex,
                                                                       @RequestBody Optional<PatchMatchDTO> matchInfo) {
        log.info("Patching match with id {}", matchIndex);
        return ResponseEntity.ok().body(bracketsService.patchBracketsMatchState(
                bracketsId,
                matchId,
                matchInfo)));
    }

    @GetMapping("/{bracketsId}")
    public ResponseEntity<List<BracketsMatchDTO>> getAllMatches(@PathVariable Long bracketsId) {
        log.info("Retrieving all matches");
        return ResponseEntity.ok().body(bracketsService.getAllMatches(bracketsId).stream()
                .map(MappingUtils::mapToBracketsMatchDTO)
                .toList());
    }

    @GetMapping("/{bracketsId}/available_matches")
    public ResponseEntity<Set<BracketsMatchDTO>> getAvailableMatches(@PathVariable Long bracketsId) {
        log.info("Retrieving available matches");
        return ResponseEntity.ok().body(bracketsService.getAvailableMatches(bracketsId).stream()
                .map(MappingUtils::mapToBracketsMatchDTO)
                .collect(Collectors.toSet()));
    }
}
