package university.innopolis.tabletennis.tournamentmicroservice.testingutils;

import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;

import java.util.List;

public class TestDTOs {
    public static TournamentDTO getTestTournament() {
        return TournamentDTO.builder()
                .title("Test Tournament")
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
