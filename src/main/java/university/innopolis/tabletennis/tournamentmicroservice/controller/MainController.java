package university.innopolis.tabletennis.tournamentmicroservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;

import java.util.List;

@RestController
public class MainController {
    @Autowired
    private PlayerRepository repository;

    private TournamentService service;

    @GetMapping("/hello")
    public List<Player> sayHello() {
        System.out.println(repository.findByName("Ivan"));
        return repository.findAll();
    }


}
