package university.innopolis.tabletennis.tournamentmicroservice.controller;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PlayersListRequest;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.TournamentRequest;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.List;
import java.util.Optional;

@RestController
@NoArgsConstructor
public class MainController {

    @Autowired
    private TournamentService service;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/players")
    public List<Player> getPlayers() {
        return service.retrievePlayers();
    }

    @PostMapping("/add_players")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Player> postPlayers(@RequestBody PlayersListRequest playersList) {
        return service.addPlayers(playersList);
    }

    @PostMapping("/create_tournament")
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament postTournament(@RequestBody TournamentRequest tournamentToAdd) {
        return service.addTournament(tournamentToAdd);
    }

    @PostMapping("/add_player")
    @ResponseStatus(HttpStatus.CREATED)
    public Player postPlayer(@RequestBody Player playerToAdd) {
        return service.addPlayer(playerToAdd);
    }

    @GetMapping("/tournaments")
    public List<Tournament> getTournaments() {
        return service.retrieveTournaments();
    }

    @GetMapping("/matches")
    public List<Match> getAllMatches() {
        return service.retrieveMatches();
    }

    @GetMapping("/tables")
    public List<GameTable> getAllTables() {
        return service.retrieveGameTables();
    }

    @GetMapping("/rounds")
    public List<Round> getAllRounds() {
        return service.retrieveRound();
    }

//    @PatchMapping("/player/set_playing/{id}")
//    public Player patchPlayerIsPlaying(@PathVariable Long id) {
//        return service.patchPlayingStatus(id);
//    }
//
    @PatchMapping("/match/is_playing/{id}")
    public Match patchMatchIsBeingPlayed(@PathVariable Long id) {
        return service.setMatchIsBeingPlayed(id);
    }

    @PatchMapping("/match/is_completed/{id}")
    public Match patchMatchIsCompleted(@PathVariable Long id) {
        return service.setMatchIsCompleted(id);
    }

    @GetMapping("/matches_available")
    public List<Match> getAvailableMatches() {
        return service.retrieveAvailableMatches();
    }
}
