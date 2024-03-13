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

    @PostConstruct
    private void init() {
    }
}
