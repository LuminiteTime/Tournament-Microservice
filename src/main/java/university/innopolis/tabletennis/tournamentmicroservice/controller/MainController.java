package university.innopolis.tabletennis.tournamentmicroservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.dto.GameTableDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.service.MatchService;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/tournaments")
public class MainController {

    private final TournamentService tournamentService;

    private final MatchService matchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TournamentDTO> postTournament(@Valid @RequestBody TournamentDTO tournamentDTO) {
        log.info("Creating a new tournament: {}", tournamentDTO);
        return new ResponseEntity<>(MappingUtils.mapToTournamentDTO(
                tournamentService.addTournament(tournamentDTO)), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        log.info("Retrieving all tournaments");
        return ResponseEntity.ok().body(tournamentService.retrieveAllTournaments().stream()
                .map(MappingUtils::mapToTournamentDTO)
                .toList());
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable Long tournamentId) {
        log.info("Retrieving tournament with id: {}", tournamentId);
        return ResponseEntity.ok().body(MappingUtils.mapToTournamentDTO(tournamentService.retrieveTournament(tournamentId)));
    }

    @PatchMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> patchTournament(@PathVariable Long tournamentId) {
        log.info("Patching state of the tournament with id: {}", tournamentId);
        return ResponseEntity.ok().body(MappingUtils.mapToTournamentDTO(tournamentService.patchTournamentState(tournamentId)));
    }

    @GetMapping("/{tournamentId}/tables")
    public ResponseEntity<List<GameTableDTO>> getAllTables(@PathVariable Long tournamentId) {
        log.info("Retrieving all tables of the tournament with id: {}", tournamentId);
        return ResponseEntity.ok().body(tournamentService.retrieveGameTables(tournamentId).stream()
                .map(MappingUtils::mapToGameTableDTO)
                .toList());
    }

    @PatchMapping("/{tournamentId}/match/{matchId}")
    public ResponseEntity<MatchDTO> patchMatchState(@PathVariable Long tournamentId,
                                                    @PathVariable Long matchId,
                                                    @RequestBody Optional<PatchMatchDTO> matchInfo) {
        log.info("Patching state of the match with id: {} in tournament with id: {}", matchId, tournamentId);
        tournamentService.retrieveTournament(tournamentId);
        return ResponseEntity.ok().body(matchService.patchMatchState(matchId, matchInfo));
    }

    @GetMapping("/{tournamentId}/tables/{tableId}/matches_available")
    public ResponseEntity<List<MatchDTO>> getAvailableMatches(@PathVariable Long tournamentId,
                                                              @PathVariable Long tableId) {
        log.info("Retrieving available matches for table with id: {} in tournament with id: {}", tableId, tournamentId);
        if (tournamentService.retrieveTournament(tournamentId).getState() != TournamentState.PLAYING) {
            log.warn("Invalid tournament state. Tournament with id {} is not being played", tournamentId);
            throw new IllegalArgumentException("Tournament with id " + tournamentId + " is not being played.");
        }
        return ResponseEntity.ok().body(matchService.retrieveAvailableMatches(tableId));
    }


    @Operation(
            summary = "Get all matches of the tournament",
            description = "Allow to get all matches of the tournament by its id"
    )
    @GetMapping("/{tournamentId}/matches")
    public ResponseEntity<List<MatchDTO>> getAllMatches(@PathVariable @Parameter(description = "Id of the tournament") Long tournamentId) {
        log.info("Retrieving all matches of the tournament with id: {}", tournamentId);
        return ResponseEntity.ok().body(
                tournamentService.retrieveALlMatches(tournamentId).stream()
                        .map(MappingUtils::mapToMatchDTO)
                        .toList()
        );
    }
}

