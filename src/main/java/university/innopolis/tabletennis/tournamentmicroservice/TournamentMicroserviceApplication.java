package university.innopolis.tabletennis.tournamentmicroservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchesRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class TournamentMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentMicroserviceApplication.class, args);
	}

	private final PlayerRepository playerRepository;
	private final MatchesRepository matchesRepository;
	@PostConstruct
	private void init(){
		Player pl1 = new Player("Nastya", 65);
		Player pl2 = new Player("Ivan", 20);
		Player pl3 = new Player("Misha", 50);
		Player pl4 = new Player("Arsen", 80);

		playerRepository.saveAll(List.of(
				pl1,
				pl2,
				pl3,
				pl4
		));

		matchesRepository.saveAll(List.of(
				new Match(pl1, pl2),
				new Match(pl1, pl3),
				new Match(pl1, pl4),
				new Match(pl2, pl3),
				new Match(pl2, pl4),
				new Match(pl3, pl4)
		));
	}
}
