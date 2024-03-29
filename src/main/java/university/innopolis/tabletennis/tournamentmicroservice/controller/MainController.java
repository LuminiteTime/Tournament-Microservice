package university.innopolis.tabletennis.tournamentmicroservice.controller;


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
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.service.MatchService;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TournamentDTO> postTournament(@Valid @RequestBody TournamentDTO tournamentDTO) {
        return ResponseEntity.ok().body(tournamentService.addTournament(tournamentDTO));}

    @GetMapping
    public ResponseEntity<List<TournamentDTO>> getTournaments() {
        return ResponseEntity.ok().body(tournamentService.retrieveAllTournaments());
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> getTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok().body(tournamentService.retrieveTournament(tournamentId));
    }

    @PatchMapping("/{tournamentId}")
    public ResponseEntity<TournamentDTO> patchTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok().body(tournamentService.patchTournamentState(tournamentId));
    }

    @GetMapping("/{tournamentId}/tables")
    public ResponseEntity<List<GameTableDTO>> getAllTables(@PathVariable Long tournamentId) {
        return ResponseEntity.ok().body(tournamentService.retrieveGameTables(tournamentId));
    }

    @PatchMapping("/{tournamentId}/match/{matchId}")
    public ResponseEntity<MatchDTO> patchMatchState(@PathVariable Long tournamentId,
                                                    @PathVariable Long matchId,
                                                    @RequestBody Optional<PatchMatchRequestBody> matchInfo) {
        return ResponseEntity.ok().body(matchService.patchMatchState(matchId, matchInfo));
    }

    @GetMapping("/{tournamentId}/tables/{tableId}/matches_available")
    public ResponseEntity<List<MatchDTO>> getAvailableMatches(@PathVariable Long tournamentId,
                                                              @PathVariable Long tableId) {
        return ResponseEntity.ok().body(matchService.retrieveAvailableMatches(tableId));
    }
}
