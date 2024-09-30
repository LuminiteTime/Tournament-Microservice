package university.innopolis.tabletennis.tournamentmicroservice.testingutils;

import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;

import java.util.List;

public class TestData {

    public static final Long INVALID_ID = -1L;
    public static final int MATCHES_AMOUNT = 12;
    public static final int AVAILABLE_MATCHES_AMOUNT_FOR_ONE_TABLE = 2;

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

    public static PatchMatchDTO getTestPatchMatchData() {
        return PatchMatchDTO.builder()
                .firstPlayerScore(11)
                .secondPlayerScore(6)
                .build();
    }
}
