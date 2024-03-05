package university.innopolis.tabletennis.tournamentmicroservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.entity.GameTable;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.GameTableRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.RoundRepository;

import java.util.ArrayList;
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
//        Player player9 = new Player("Daniel Anderson", 122);

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
        Integer numberOfPlayers = playersList.size();

        playerRepository.saveAll(playersList);

        // TODO: START

        List<GameTable> tablesOfTournament = new ArrayList<>();
        GameTable gameTableEight = new GameTable();
//        playersList.sort(Comparator.comparing(Player::getRating));
        for (int i = playersList.size() - 1; i >= 0; i--) {
            Player player = playersList.get(i);
            gameTableEight.addPlayer(player);
        }
        gameTableEight.fillRounds();
        matchRepository.saveAll(gameTableEight.getMatchesOfTable());
        roundRepository.saveAll(gameTableEight.getRoundsOfTable());
//        tablesOfTournament.add(gameTableEight);
//
//        gameTableRepository.saveAll(tablesOfTournament);
//        tableRepository.save(tableEight);

        // TODO: END
    }
}
