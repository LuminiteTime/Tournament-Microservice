package university.innopolis.tabletennis.tournamentmicroservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.entity.GameTable;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Round;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.GameTableRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.RoundRepository;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private GameTableRepository tableRepository;

    @Autowired
    private RoundRepository roundRepository;

    private TournamentService service;

    public MainController() {
    }

    @GetMapping("/hello")
    public List<Player> sayHello() {
        return playerRepository.findAll();
    }

    @GetMapping("/get_all_matches")
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping("/get_tables")
    public List<GameTable> getAllTables() {
        return tableRepository.findAll();
    }

    @GetMapping("/get_rounds")
    public List<Round> getAllRounds() {
        return roundRepository.findAll();
    }
}
