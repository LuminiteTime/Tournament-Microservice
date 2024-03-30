package university.innopolis.tabletennis.tournamentmicroservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.dto.GameTableDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.service.MatchService;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(
        name = "Main Controller",
        description = "Controls creation of tournaments and operations on matches," +
                " game tables, and tournaments."
)
@RestController
@NoArgsConstructor
@RequestMapping("/tournaments")
public class MainController {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchService matchService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @Operation(
            summary = "Create tournament",
            description = "Allows to create a tournament"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TournamentDTO> postTournament(@Valid @RequestBody @Parameter TournamentDTO tournamentDTO) {
        return ResponseEntity.ok().body(tournamentService.addTournament(tournamentDTO));}

    @Operation(
            summary = "Get all tournaments",
            description = "Allows to get all tournaments"
    )
    @GetMapping
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        return ResponseEntity.ok().body(tournamentService.retrieveAllTournaments());
    }

    @Operation(
            summary = "Get a tournament",
            description = "Allows to get a tournament by id"
    )
    @GetMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable @Parameter(description = "Id of a tournament to get") Long tournamentId) {
        return ResponseEntity.ok().body(tournamentService.retrieveTournament(tournamentId));
    }

    @Operation(
            summary = "Finish the tournament",
            description = "Allows to finish the tournament by id"
    )
    @PatchMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> patchTournament(@PathVariable @Parameter(description = "Id of a tournament to finish") Long tournamentId) {
        return ResponseEntity.ok().body(tournamentService.patchTournamentState(tournamentId));
    }

    @Operation(
            summary = "Get all game tables of the tournament",
            description = "Allows to get all game tables of the tournament by id"
    )
    @GetMapping("/{tournamentId}/tables")
    public ResponseEntity<List<GameTableDTO>> getAllTables(@PathVariable @Parameter(description = "Id of the tournament") Long tournamentId) {
        return ResponseEntity.ok().body(tournamentService.retrieveGameTables(tournamentId));
    }

    @Operation(
            summary = "Make the match started if not started and completed if started",
            description = "Allows to make the match started if not started and completed if started"
    )
    @PatchMapping("/{tournamentId}/match/{matchId}")
    public ResponseEntity<MatchDTO> patchMatchState(@PathVariable @Parameter(description = "Id of the tournament") Long tournamentId,
                                                    @PathVariable @Parameter(description = "Id of the match") Long matchId,
                                                    @RequestBody @Parameter(description = "Match score to mark match as completed") Optional<PatchMatchDTO> matchInfo) {
        return ResponseEntity.ok().body(matchService.patchMatchState(matchId, matchInfo));
    }

    @Operation(
            summary = "Get matches available to be played",
            description = "Allows to get matches ready to be played"
    )
    @GetMapping("/{tournamentId}/tables/{tableId}/matches_available")
    public ResponseEntity<List<MatchDTO>> getAvailableMatches(@PathVariable @Parameter(description = "Id of the tournament") Long tournamentId,
                                                              @PathVariable @Parameter(description = "Id of the table with matches") Long tableId) {
        return ResponseEntity.ok().body(matchService.retrieveAvailableMatches(tableId));
    }
}

