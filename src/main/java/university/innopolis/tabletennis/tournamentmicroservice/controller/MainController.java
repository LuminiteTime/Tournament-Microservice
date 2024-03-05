package university.innopolis.tabletennis.tournamentmicroservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchesRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchesRepository matchesRepository;

    private TournamentService service;

    public MainController() {
    }

    @GetMapping("/hello")
    public List<Player> sayHello() {
        return playerRepository.findAll();
    }

    @GetMapping("/all_matches")
    public List<Match> getAllMatches() {
        return matchesRepository.findAll();
    }
}
