package university.innopolis.tabletennis.tournamentmicroservice.controller;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.IdListRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PostPlayersListRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.List;

@RestController
@NoArgsConstructor
@RequestMapping("/tournaments")
public class MainController {

    @Autowired
    private TournamentService service;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/tournament")
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament postTournament() {
        return service.addTournament();
    }

    @GetMapping("/")
    public List<Tournament> getTournaments(@RequestBody IdListRequestBody tournamentsList) {
        return service.retrieveTournaments(tournamentsList);
    }

    @GetMapping("/{tournamentId}/players")
    public List<Player> getPlayers(@PathVariable Long tournamentId) {
        return service.retrievePlayers(tournamentId);
    }

    @PostMapping("/{tournamentId}/players")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Player> postPlayers(@RequestBody PostPlayersListRequestBody playersList,
                                    @PathVariable Long tournamentId) {
        return service.addPlayers(tournamentId, playersList);
    }

    @DeleteMapping("/{tournamentId}/players/{playerId}")
    public Player deletePlayer(@PathVariable Long tournamentId,
                               @PathVariable Long playerId) {
        return service.deletePlayer(tournamentId, playerId);
    }

    @GetMapping("{tournamentId}/matches")
    public List<Match> getAllMatches(@PathVariable Long tournamentId) {
        return service.retrieveMatches(tournamentId);
    }

    @GetMapping("{tournamentId}/tables")
    public List<GameTable> getAllTables(@PathVariable Long tournamentId) {
        return service.retrieveGameTables(tournamentId);
    }

    // TODO: Maybe not needed
    @GetMapping("/{tournamentId}/rounds")
    public List<Round> getAllRounds(@PathVariable Long tournamentId) {
        return service.retrieveRounds(tournamentId);
    }

    @PatchMapping("/{tournamentId}/match/is_playing/{matchId}")
    public Match patchMatchIsBeingPlayed(@PathVariable Long matchId,
                                         @PathVariable Long tournamentId) {
        return service.setMatchIsBeingPlayed(tournamentId, matchId);
    }

    @PatchMapping("/{tournamentId}/match/is_completed/{matchId}")
    public Match patchMatchIsCompleted(@PathVariable Long matchId,
                                       @PathVariable Long tournamentId,
                                       @RequestBody PatchMatchRequestBody matchToPatch) {
        return service.setMatchIsCompleted(tournamentId, matchId, matchToPatch);
    }

    @GetMapping("{tournamentId}/matches_available")
    public List<Match> getAvailableMatches(@PathVariable Long tournamentId) {
        return service.retrieveAvailableMatches(tournamentId);
    }
}
