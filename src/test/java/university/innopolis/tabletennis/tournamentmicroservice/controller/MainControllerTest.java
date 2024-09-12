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

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

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
    void whenSendingInvalidData_thenReturnErrorCode() {
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
                .and()
                .body("title", hasItems("Tournament 1", "Tournament 2"));
    }
}