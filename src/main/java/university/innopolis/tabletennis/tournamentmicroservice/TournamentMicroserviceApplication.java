package university.innopolis.tabletennis.tournamentmicroservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class TournamentMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TournamentMicroserviceApplication.class, args);
	}

	private final PlayerRepository repo;
	@PostConstruct
	private void init(){
		repo.saveAll(List.of(
				new Player("Ivan"),
				new Player("Nastya")
		));
	}
}
