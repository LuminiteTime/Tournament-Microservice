package university.innopolis.tabletennis.tournamentmicroservice;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
