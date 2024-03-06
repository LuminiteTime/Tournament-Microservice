package university.innopolis.tabletennis.tournamentmicroservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.GameTableRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.RoundRepository;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class TournamentMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TournamentMicroserviceApplication.class, args);
    }

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final GameTableRepository gameTableRepository;
    private final RoundRepository roundRepository;

    @PostConstruct
    private void init() {

        List<Player> playersList = List.of(
                new Player("John Smith", 135),
                new Player("Emily Davis", 72),
                new Player("Michael Johnson", 189),
                new Player("Olivia White", 47),
                new Player("David Brown", 104),
                new Player("Sophia Taylor", 81),
                new Player("Benjamin Miller", 163),
                new Player("Ava Wilson", 36)
        );
        playerRepository.saveAll(playersList);


    }
}
