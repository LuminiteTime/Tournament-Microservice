package university.innopolis.tabletennis.tournamentmicroservice.testingutils;

import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;

import javax.sound.midi.Patch;
import java.util.List;
import java.util.Map;

public class TestData {

    public static final Long INVALID_ID = -1L;
    public static final int MATCHES_AMOUNT = 12;
    public static final int AVAILABLE_MATCHES_AMOUNT_FOR_ONE_TABLE = 2;
    public static final Map<Long, Integer> PLAYERS_SCORES = Map.of(
            101L, 11,
            102L, 10,
            103L, 9,
            104L, 8,
            105L, 7,
            106L, 6,
            107L, 5,
            108L, 4,
            109L, 3
    );

    public static TournamentDTO getTestTournament() {
        return getTestTournament("Test Tournament");
    }

    public static TournamentDTO getDifferentSizeTablesTestTournament() {
        return getDifferentSizeTablesTestTournament("Test Tournament");
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

    public static TournamentDTO getDifferentSizeTablesTestTournament(String title) {
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
                                new PlayerDTO(108L),
                                new PlayerDTO(109L)
                        )
                )
                .build();
    }

    public static TournamentDTO getSmallestTestTournament() {
        return TournamentDTO.builder()
                .title("Smallest Test Tournament")
                .amountOfTables(1)
                .players(
                        List.of(
                                new PlayerDTO(101L),
                                new PlayerDTO(102L),
                                new PlayerDTO(103L)
                        )
                )
                .build();
    }

    public static PatchMatchDTO getTestPatchMatchData(int firstPlayerScore, int secondPlayerScore) {
        return PatchMatchDTO.builder()
                .firstPlayerScore(firstPlayerScore)
                .secondPlayerScore(secondPlayerScore)
                .build();
    }

    public static PatchMatchDTO getTestPatchMatchData() {
        return getTestPatchMatchData(11, 6);
    }
}
