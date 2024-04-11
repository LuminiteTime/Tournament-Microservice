package university.innopolis.tabletennis.tournamentmicroservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

@Tag(name = "Main Controller", description = "Controls creation of tournaments and operations on matches," + " game tables, and tournaments.")
@RestController
@AllArgsConstructor
@RequestMapping("/tournaments")
public class MainController {

    private final TournamentService tournamentService;

    private final MatchService matchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TournamentDTO> postTournament(@Valid @RequestBody TournamentDTO tournamentDTO) {
        List<Player> playersToAdd = tournamentDTO.getPlayers().stream()
                .map(MappingUtils::mapToPlayerEntity)
                .toList();
        return ResponseEntity.ok().body(MappingUtils.mapToTournamentDTO(
                tournamentService.addTournament(tournamentDTO, playersToAdd)));
    }

    @GetMapping
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        return ResponseEntity.ok().body(tournamentService.retrieveAllTournaments().stream()
                .map(MappingUtils::mapToTournamentDTO)
                .toList());
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok().body(MappingUtils.mapToTournamentDTO(tournamentService.retrieveTournament(tournamentId)));
    }

    @PatchMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> patchTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok().body(MappingUtils.mapToTournamentDTO(tournamentService.patchTournamentState(tournamentId)));
    }

    @GetMapping("/{tournamentId}/tables")
    public ResponseEntity<List<GameTableDTO>> getAllTables(@PathVariable Long tournamentId) {
        return ResponseEntity.ok().body(tournamentService.retrieveGameTables(tournamentId).stream()
                .map(MappingUtils::mapToGameTableDTO)
                .toList());
    }

    @PatchMapping("/{tournamentId}/match/{matchId}")
    public ResponseEntity<MatchDTO> patchMatchState(@PathVariable Long tournamentId, @PathVariable Long matchId, @RequestBody Optional<PatchMatchDTO> matchInfo) {
        return ResponseEntity.ok().body(matchService.patchMatchState(matchId, matchInfo));
    }

    @GetMapping("/{tournamentId}/tables/{tableId}/matches_available")
    public ResponseEntity<List<MatchDTO>> getAvailableMatches(@PathVariable Long tournamentId, @PathVariable Long tableId) {
        if (tournamentService.retrieveTournament(tournamentId).getState() != TournamentState.PLAYING) {
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
        return ResponseEntity.ok().body(
                tournamentService.retrieveALlMatches(tournamentId).stream()
                        .map(MappingUtils::mapToMatchDTO)
                        .toList()
        );
    }
}

