package university.innopolis.tabletennis.tournamentmicroservice.testingutils;

import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;

import java.util.List;

public class TestDTOs {
    public static TournamentDTO getTestTournament() {
        return getTestTournament("Test Tournament");
    }

    public static TournamentDTO getTestTournament(String title) {
        return TournamentDTO.builder()
                .title(title)
                .amountOfTables(1)
                .players(
                        List.of(
                                new PlayerDTO(101L),
                                new PlayerDTO(102L)
                        )
                )
                .build();
    }
}
