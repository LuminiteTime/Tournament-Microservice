package university.innopolis.tabletennis.tournamentmicroservice.controller;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.IdListRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.TournamentInfo;
import university.innopolis.tabletennis.tournamentmicroservice.service.MatchService;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.List;
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
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // TODO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament postTournament(@RequestBody TournamentInfo tournamentInfo) {
        return tournamentService.addTournament(tournamentInfo);
    }

    @GetMapping
    public List<Tournament> getTournaments(@RequestBody IdListRequestBody tournamentsList) {
        return tournamentService.retrieveTournaments(tournamentsList);
    }

//    public ResponseEntity<List<Tournament>> getTournaments2(@RequestBody IdListRequestBody tournamentsList) {
//        return ResponseEntity.ok(tournamentService.retrieveTournaments(tournamentsList));
//    }

    @GetMapping("{tournamentId}/tables")
    public List<GameTable> getAllTables(@PathVariable Long tournamentId) {
        return tournamentService.retrieveGameTables(tournamentId);
    }

    @PatchMapping("/{tournamentId}/match/{matchId}")
    public Match patchMatchState(@PathVariable Long tournamentId,
                                 @PathVariable Long matchId,
                                 @RequestBody Optional<PatchMatchRequestBody> matchInfo) {
        return matchService.patchMatchState(tournamentId, matchId, matchInfo);
    }

    @GetMapping("{tournamentId}/matches_available")
    public List<Match> getAvailableMatches(@PathVariable Long tournamentId) {
        return tournamentService.retrieveAvailableMatches(tournamentId);
    }
}
