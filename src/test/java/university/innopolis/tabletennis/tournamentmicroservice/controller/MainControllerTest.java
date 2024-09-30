package university.innopolis.tabletennis.tournamentmicroservice.controller;

import io.restassured.common.mapper.TypeRef;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import university.innopolis.tabletennis.tournamentmicroservice.dto.GameTableDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.GameTable;
import university.innopolis.tabletennis.tournamentmicroservice.entity.TablesMatch;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Tournament;
import university.innopolis.tabletennis.tournamentmicroservice.exception.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.GameTableRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.TablesMatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.TournamentRepository;
import university.innopolis.tabletennis.tournamentmicroservice.service.MatchService;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import java.util.List;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static university.innopolis.tabletennis.tournamentmicroservice.testingutils.TestData.*;
import static university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils.mapToMatchDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TablesMatchRepository tablesMatchRepository;

    @Autowired
    private GameTableRepository gameTableRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_tournaments")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @AfterEach
    void afterEach() {
        tournamentRepository.deleteAll();
        gameTableRepository.deleteAll();
        tablesMatchRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Transactional
    @Test
    void whenSendingListOfPlayers_thenTournamentIsCreated() {
        TournamentDTO tournamentDTO = getTestTournament();
        given()
                .port(port)
                .contentType("application/json")
                .body(tournamentDTO)
                .when()
                .post("/tournaments")
                .then()
                .statusCode(201);
        assertEquals(1, tournamentRepository.findAll().size());

        Tournament tournament = tournamentRepository.findAll().get(0);
        assertEquals(2, tournament.getTablesOfTournament().size());

        for (GameTable gameTable : tournament.getTablesOfTournament()) {
            assertEquals(4, gameTable.getPlayers().size());
        }

        assertNotEquals(tournament.getTablesOfTournament().get(0).getPlayers(),
                tournament.getTablesOfTournament().get(1).getPlayers());

        assertEquals(tournamentDTO.getPlayers().size(), tournament.getTablesOfTournament().stream()
                .mapToInt(gameTable -> gameTable.getPlayers().size()).sum());

        assertEquals(MATCHES_AMOUNT, tournament.getTablesOfTournament().stream()
                .mapToInt(gameTable -> gameTable.getTablesMatches().size()).sum());
    }

    @Test
    void whenAmountOfTablesIsBiggerThanPlayersAmount_thenReturnErrorMessage() {
        TournamentDTO tournamentDTO = getTestTournament();
        tournamentDTO.setAmountOfTables(100);
        given()
                .port(port)
                .contentType("application/json")
                .body(tournamentDTO)
                .when()
                .post("/tournaments")
                .then()
                .statusCode(400)
                .body("message", equalTo(IllegalDesiredNumberOfTablesException.MESSAGE_TEMPLATE));
        assertEquals(0, tournamentRepository.findAll().size());
    }

    @Test
    void whenSendingTournamentDataWithExistingTitle_thenReturnErrorCode() {
        TournamentDTO tournamentDTO = getTestTournament();
        given()
                .port(port)
                .contentType("application/json")
                .body(tournamentDTO)
                .post("/tournaments");
        given()
                .port(port)
                .contentType("application/json")
                .body(tournamentDTO)
                .when()
                .post("/tournaments")
                .then()
                .statusCode(400);
        assertEquals(1, tournamentRepository.findAll().size());
    }

    @Test
    void whenRequestingTournaments_thenReturnAllTournaments() {
        TournamentDTO tournamentDTO1 = getTestTournament("Tournament 1");
        TournamentDTO tournamentDTO2 = getTestTournament("Tournament 2");
        tournamentService.addTournament(tournamentDTO1);
        tournamentService.addTournament(tournamentDTO2);
        List<TournamentDTO> listOfTournamentsDTOsResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get("/tournaments")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        System.out.println(listOfTournamentsDTOsResponseBody.get(0).toString());
        assertArrayEquals(new String[]{tournamentDTO1.getTitle(), tournamentDTO2.getTitle()},
                listOfTournamentsDTOsResponseBody.stream()
                        .map(TournamentDTO::getTitle)
                        .toArray());
    }

    @Test
    void whenRequestingTournamentById_thenReturnTournamentById() {
        TournamentDTO tournamentDTO = getTestTournament();
        Tournament tournament = tournamentService.addTournament(tournamentDTO);
        TournamentDTO tournamentDTOResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("/tournaments/%d", tournament.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TournamentDTO.class);
        assertEquals(tournamentDTO.getTitle(), tournamentDTOResponseBody.getTitle());
    }

    @Test
    void whenRequestingTournamentByInvalidId_thenReturnError() {
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("tournaments/%d", INVALID_ID))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(String.format(TournamentNotFoundException.MESSAGE_TEMPLATE, INVALID_ID),
                errorResponseBody.getMessage());
    }

    @Test
    void whenFinishingTournamentByIdWithNoAvailableMatchesLeft_thenChangeStatusToFinished() {
        TournamentDTO tournamentDTO = getTestTournament();
        Tournament tournament = tournamentService.addTournament(tournamentDTO);
        Long tournamentId = tournament.getId();

        playAllAvailableMatches(tournament);

        TournamentDTO tournamentDTOResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch(String.format("/tournaments/%d", tournamentId))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TournamentDTO.class);
        assertEquals(TournamentState.FINISHED, tournamentDTOResponseBody.getState());
    }

    @Test
    void whenFinishingFinishedTournamentById_thenNothingChanges() {
        TournamentDTO tournamentDTO = getTestTournament();
        Tournament tournament = tournamentService.addTournament(tournamentDTO);
        playAllAvailableMatches(tournament);
        tournament.setState(TournamentState.FINISHED);
        TournamentDTO tournamentDTOResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch(String.format("/tournaments/%d", tournament.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TournamentDTO.class);
        assertEquals(TournamentState.FINISHED, tournamentDTOResponseBody.getState());
    }

    @Test
    void whenFinishingTournamentByInvalidId_thenReturnError() {
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch(String.format("tournaments/%d", INVALID_ID))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(String.format(TournamentNotFoundException.MESSAGE_TEMPLATE, INVALID_ID),
                errorResponseBody.getMessage());
    }

    @Test
    void whenFinishingTournamentWithAvailableMatchesLeft_thenReturnError() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch(String.format("tournaments/%d", tournament.getId()))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(String.format(UnableToFinishTournamentException.MESSAGE_TEMPLATE, tournament.getId()),
                errorResponseBody.getMessage());
    }

    @Test
    void whenRequestingGameTablesOfTournamentById_thenReturnListOfGameTables() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        List<GameTableDTO> listOfGameTablesResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("tournaments/%d/tables", tournament.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(2, listOfGameTablesResponseBody.size());
    }

    @Test
    void whenRequestingGameTablesOfInvalidTournamentId_thenReturnError() {
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("/tournaments/%d/tables", INVALID_ID))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(String.format(TournamentNotFoundException.MESSAGE_TEMPLATE, INVALID_ID),
                errorResponseBody.getMessage());
    }

    @Test
    void whenPatchingMatchToStart_thenChangeMatchStateToPLAYING() {
        TournamentDTO tournamentDTO = getTestTournament();
        Tournament tournament = tournamentService.addTournament(tournamentDTO);
        Long matchId = matchService.retrieveAvailableMatches(tournament.getTablesOfTournament().get(0).getId()).get(0).getId();
        MatchDTO matchDTOResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch(String.format("/tournaments/%d/match/%d",
                        tournament.getId(),
                        matchId))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(MatchDTO.class);
        assertEquals(matchId, matchDTOResponseBody.getId());
        assertEquals(MatchState.PLAYING, matchDTOResponseBody.getState());
    }

    @Test
    void whenPatchingPlayingMatchToComplete_thenChangeMatchStateToCOMPLETED() {
        TournamentDTO tournamentDTO = getTestTournament();
        Tournament tournament = tournamentService.addTournament(tournamentDTO);
        Long matchId = matchService.retrieveAvailableMatches(tournament.getTablesOfTournament().get(0).getId()).get(0).getId();
        given()
                .port(port)
                .contentType("application/json")
                .patch(String.format("/tournaments/%d/match/%d", tournament.getId(), matchId));
        MatchDTO matchDTOResponseBody = given()
                .port(port)
                .contentType("application/json")
                .body(getTestPatchMatchData())
                .when()
                .patch(String.format("/tournaments/%d/match/%d", tournament.getId(), matchId))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(MatchDTO.class);
        assertEquals(matchId, matchDTOResponseBody.getId());
        assertEquals(MatchState.COMPLETED, matchDTOResponseBody.getState());
    }

    @Test
    void whenPatchingMatchWithInvalidId_thenReturnError() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch(String.format("/tournaments/%d/match/%d", tournament.getId(), INVALID_ID))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(String.format(MatchNotFoundException.MESSAGE_TEMPLATE, INVALID_ID),
                errorResponseBody.getMessage());
    }

    @Test
    void whenRequestingMatchesOfTournamentById_thenReturnListOfMatches() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        List<MatchDTO> listOfMatchDTOsResponseBody = given()
                .port(port)
                .contentType("application/type")
                .when()
                .get(String.format("tournaments/%d/matches", tournament.getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(MATCHES_AMOUNT, listOfMatchDTOsResponseBody.size());
    }

    @Test
    void whenRequestingMatchesOfTournamentByInvalidId_thenReturnError() {
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/type")
                .when()
                .get(String.format("tournaments/%d/matches", INVALID_ID))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(String.format(TournamentNotFoundException.MESSAGE_TEMPLATE, INVALID_ID),
                errorResponseBody.getMessage());
    }

    @Test
    void whenRequestingAvailableMatchesOfTournamentById_thenReturnListOfMatches() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        List<MatchDTO> listOfMatchDTOsResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("tournaments/%d/tables/%d/matches_available",
                        tournament.getId(),
                        tournament.getTablesOfTournament().get(0).getId()))
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(AVAILABLE_MATCHES_AMOUNT_FOR_ONE_TABLE, listOfMatchDTOsResponseBody.size());
    }

    @Test
    void whenRequestingAvailableMatchesOfTournamentByInvalidId_thenReturnError() {
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("tournaments/%d/tables/%d/matches_available",
                        INVALID_ID,
                        INVALID_ID))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(String.format(TournamentNotFoundException.MESSAGE_TEMPLATE, INVALID_ID),
                errorResponseBody.getMessage());
    }

    @Test
    void whenRequestingAvailableMatchesOfTournamentByInvalidTableId_thenReturnError() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        ErrorResponse errorResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("tournaments/%d/tables/%d/matches_available",
                        tournament.getId(),
                        INVALID_ID))
                .then()
                .statusCode(400)
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(String.format(GameTableNotFoundException.MESSAGE_TEMPLATE, INVALID_ID),
                errorResponseBody.getMessage());
    }

    @Test
    void whenMatchStarted_thenAmountOfAvailableMatchesIsReducedByOne() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        given()
                .port(port)
                .contentType("application/json")
                .patch(String.format("tournaments/%d/match/%d",
                        tournament.getId(),
                        matchService.retrieveAvailableMatches(tournament.getTablesOfTournament().get(0).getId()).get(0).getId()));
        List<MatchDTO> listOfMatchDTOsResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("tournaments/%d/tables/%d/matches_available",
                        tournament.getId(),
                        tournament.getTablesOfTournament().get(0).getId()))
                .then()
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(AVAILABLE_MATCHES_AMOUNT_FOR_ONE_TABLE - 1,
                listOfMatchDTOsResponseBody.size());
    }

    @Test
    void whenMatchStartedAndCompleted_thenAmountOfAvailableMatchesIsReducedByOne() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        Long matchId = matchService.retrieveAvailableMatches(tournament.getTablesOfTournament().get(0).getId()).get(0).getId();
        given()
                .port(port)
                .contentType("application/json")
                .patch(String.format("tournaments/%d/match/%d",
                        tournament.getId(),
                        matchId));
        given()
                .port(port)
                .contentType("application/json")
                .body(getTestPatchMatchData())
                .when()
                .patch(String.format("tournaments/%d/match/%d",
                        tournament.getId(),
                        matchId));
        List<MatchDTO> listOfMatchDTOsResponseBody = given()
                .port(port)
                .contentType("application/json")
                .when()
                .get(String.format("tournaments/%d/tables/%d/matches_available",
                        tournament.getId(),
                        tournament.getTablesOfTournament().get(0).getId()))
                .then()
                .extract()
                .body()
                .as(new TypeRef<>() {
                });
        assertEquals(AVAILABLE_MATCHES_AMOUNT_FOR_ONE_TABLE - 1,
                listOfMatchDTOsResponseBody.size());
        TablesMatch match = tablesMatchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException(matchId));
        assertFalse(listOfMatchDTOsResponseBody.contains(mapToMatchDTO(match)));
    }

    @Test
    void whenCompletedTwoAvailableMatchesInTableOfFourPlayers_thenAvailableMatchesAreAllNewMatches() {
        Tournament tournament = tournamentService.addTournament(getTestTournament());
        Long tableId = tournament.getTablesOfTournament().get(0).getId();
        List<MatchDTO> oldAvailableMatches = matchService.retrieveAvailableMatches(tableId);
        for (MatchDTO match : oldAvailableMatches) {
            Long matchId = match.getId();
            given()
                    .port(port)
                    .contentType("application/json")
                    .patch(String.format("tournaments/%d/match/%d",
                            tournament.getId(),
                            matchId));
            given()
                    .port(port)
                    .contentType("application/json")
                    .body(getTestPatchMatchData())
                    .when()
                    .patch(String.format("tournaments/%d/match/%d",
                            tournament.getId(),
                            matchId));
        }
        List<MatchDTO> newAvailableMatches = matchService.retrieveAvailableMatches(tableId);
        assertEquals(AVAILABLE_MATCHES_AMOUNT_FOR_ONE_TABLE, newAvailableMatches.size());
        assertNotEquals(oldAvailableMatches, newAvailableMatches);
    }

    public void playAllAvailableMatches(Tournament tournament) {
        Long tournamentId = tournament.getId();
        for (GameTable table : tournament.getTablesOfTournament()) {
            List<MatchDTO> availableMatches = given()
                    .port(port)
                    .contentType("application/json")
                    .when()
                    .get(String.format("tournaments/%d/tables/%d/matches_available",
                            tournamentId,
                            table.getId()))
                    .then()
                    .extract()
                    .body()
                    .as(new TypeRef<>() {
                    });
            while (!availableMatches.isEmpty()) {
                Long matchId = availableMatches.get(0).getId();
                given()
                        .port(port)
                        .contentType("application/json")
                        .patch(String.format("tournaments/%d/match/%d",
                                tournamentId,
                                matchId));
                given()
                        .port(port)
                        .contentType("application/json")
                        .body(getTestPatchMatchData())
                        .when()
                        .patch(String.format("tournaments/%d/match/%d",
                                tournamentId,
                                matchId));
                availableMatches = matchService.retrieveAvailableMatches(table.getId());
            }
        }
    }

}
