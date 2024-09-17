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
                .amountOfTables(2)
                .players(
                        List.of(
                                new PlayerDTO(101L),
                                new PlayerDTO(102L),
                                new PlayerDTO(103L),
                                new PlayerDTO(104L),
                                new PlayerDTO(105L),
                                new PlayerDTO(106L),
                                new PlayerDTO(107L),
                                new PlayerDTO(108L)
                        )
                )
                .build();
    }
}
