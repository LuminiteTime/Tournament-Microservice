package university.innopolis.tabletennis.tournamentmicroservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.TournamentRequest;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private TournamentService service;

    public MainController() {
    }

    @GetMapping("/get_players")
    public List<Player> getPlayers() {
        return service.retrievePlayers();
    }

    @PostMapping("/post_tournament")
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament postTournament(@RequestBody TournamentRequest tournamentToAdd) {
        return service.addTournament(tournamentToAdd);
    }

    @PostMapping("/post_player")
    @ResponseStatus(HttpStatus.CREATED)
    public Player postPlayer(@RequestBody Player playerToAdd) {
        return service.addPlayer(playerToAdd);
    }

    @GetMapping("/get_tournaments")
    public List<Tournament> getTournaments() {
        return service.retrieveTournaments();
    }

    @GetMapping("/get_matches")
    public List<Match> getAllMatches() {
        return service.retrieveMatches();
    }

    @GetMapping("/get_tables")
    public List<GameTable> getAllTables() {
        return service.retrieveGameTables();
    }

    @GetMapping("/get_rounds")
    public List<Round> getAllRounds() {
        return service.retrieveRound();
    }
}
