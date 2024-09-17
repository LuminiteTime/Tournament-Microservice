package university.innopolis.tabletennis.tournamentmicroservice.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.repository.GameTableRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.TablesMatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.TournamentRepository;
import university.innopolis.tabletennis.tournamentmicroservice.service.TournamentService;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import static university.innopolis.tabletennis.tournamentmicroservice.testingutils.TestDTOs.getTestTournament;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TournamentService tournamentService;

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
//        registry.add("spring.datasource.passowrd", postgres::getPassword);
    }

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
                .body("message", equalTo("Desired number of tables must be less than the number of players minus 1"));
    }

    @Test
    void whenSendingInvalidTournamentData_thenReturnErrorCode() {
        TournamentDTO tournamentDTO = getTestTournament();
        tournamentService.addTournament(tournamentDTO);
        given()
                .port(port)
                .contentType("application/json")
                .body(tournamentDTO)
                .when()
                .post("/tournaments")
                .then()
                .statusCode(400);
    }

    @Test
    void whenRequestingTournaments_thenReturnAllTournament() {
        TournamentDTO tournamentDTO1 = getTestTournament("Tournament 1");
        TournamentDTO tournamentDTO2 = getTestTournament("Tournament 2");
        tournamentService.addTournament(tournamentDTO1);
        tournamentService.addTournament(tournamentDTO2);
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .get("/tournaments")
                .then()
                .statusCode(200)
                .body("title", hasItems("Tournament 1", "Tournament 2"));
    }

    @Test
    void whenRequestingTournamentById_thenReturnTournamentById() {
        TournamentDTO tournamentDTO = getTestTournament();
        tournamentService.addTournament(tournamentDTO);
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .get("/tournaments/1")
                .then()
                .statusCode(200)
                .body("title", equalTo("Test Tournament"));
    }

    @Test
    void whenRequestingTournamentByInvalidId_thenReturnError() {
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .get("tournaments/1")
                .then()
                .statusCode(400)
                .body("message", equalTo("Tournament with id 1 does not exist."));
    }

    @Test
    void whenFinishingTournamentById_thenChangeStatusToFinished() {
        TournamentDTO tournamentDTO = getTestTournament();
        tournamentService.addTournament(tournamentDTO);
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch("/tournaments/1")
                .then()
                .statusCode(200)
                .body("state", equalTo("FINISHED"));
    }

    @Test
    void whenFinishingFinishedTournamentById_thenNothingChanges() {
        TournamentDTO tournamentDTO = getTestTournament();
        tournamentDTO.setState(TournamentState.FINISHED);
        tournamentService.addTournament(tournamentDTO);
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch("/tournaments/1")
                .then()
                .statusCode(200)
                .body("state", equalTo("FINISHED"));
    }

    @Test
    void whenFinishingTournamentByInvalidId_thenReturnError() {
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .patch("tournaments/1")
                .then()
                .statusCode(400)
                .body("message", equalTo("Tournament with id 1 does not exist."));
    }

    @Test
    void whenRequestingGameTablesOfTournamentById_thenReturnListOfGameTables() {
        tournamentService.addTournament(getTestTournament());
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .get("tournaments/1/tables")
                .then()
                .statusCode(200)
                .body("size()", equalTo(2));
    }

    @Test
    void whenRequestingGameTablesOfInvalidTournamentId_thenReturnError() {
        given()
                .port(port)
                .contentType("application/json")
                .when()
                .get("/tournaments/999/tables")
                .then()
                .statusCode(400)
                .body("message", equalTo("Tournament with id 999 does not exist."));
    }
}