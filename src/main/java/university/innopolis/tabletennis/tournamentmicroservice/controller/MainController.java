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

    @PostMapping("/tournament")
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament postTournament() {
        return tournamentService.addTournament();
    }

    @GetMapping("/")
    public List<Tournament> getTournaments(@RequestBody IdListRequestBody tournamentsList) {
        return tournamentService.retrieveTournaments(tournamentsList);
    }

    @GetMapping("/{tournamentId}/players")
    public List<Player> getPlayers(@PathVariable Long tournamentId) {
        return tournamentService.retrievePlayers(tournamentId);
    }

    @PostMapping("/{tournamentId}/players")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Player> postPlayers(@RequestBody PostPlayersListRequestBody playersList,
                                    @PathVariable Long tournamentId) {
        return tournamentService.addPlayers(tournamentId, playersList);
    }

    @DeleteMapping("/{tournamentId}/players/{playerId}")
    public Player deletePlayer(@PathVariable Long tournamentId,
                               @PathVariable Long playerId) {
        return tournamentService.deletePlayer(tournamentId, playerId);
    }

    @GetMapping("{tournamentId}/matches")
    public List<Match> getAllMatches(@PathVariable Long tournamentId) {
        return tournamentService.retrieveMatches(tournamentId);
    }

    @GetMapping("{tournamentId}/tables")
    public List<GameTable> getAllTables(@PathVariable Long tournamentId) {
        return tournamentService.retrieveGameTables(tournamentId);
    }

    // TODO: Maybe not needed
    @GetMapping("/{tournamentId}/rounds")
    public List<Round> getAllRounds(@PathVariable Long tournamentId) {
        return tournamentService.retrieveRounds(tournamentId);
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
