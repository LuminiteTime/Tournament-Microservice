package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import university.innopolis.tabletennis.tournamentmicroservice.builderinterface.ITournamentBuilder;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.RoundsCreator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany
    private List<GameTable> tablesOfTournament;

    @OneToMany
    private List<Player> players;

    private TournamentState state;

    private LocalDate date;

    @Transient
    private List<Player> sortedBracketsPlayers;

    private Tournament(TournamentBuilder builder) {
        if (builder.desiredNumberOfTables > (builder.players.size() / 2)) {
            throw new IllegalArgumentException("Desired number of tables must be less than the number of players minus 1");
        }

        this.title = builder.title;
        this.date = LocalDate.now();
        this.state = TournamentState.PLAYING;
        this.players = builder.players;

        this.createGameTables(builder.desiredNumberOfTables);
        this.fillTables();
        for (GameTable gameTable: this.tablesOfTournament) {
            this.createRoundsWithMatches(gameTable);
        }
    }

    @NoArgsConstructor
    public static class TournamentBuilder implements ITournamentBuilder {

        private String title;
        private List<Player> players;
        private Integer desiredNumberOfTables;

        @Override
        public Tournament.TournamentBuilder title(String title) {
            this.title = title;
            return this;
        }

        @Override
        public Tournament.TournamentBuilder players(List<Player> players) {
            this.players = players;
            return this;
        }

        public Tournament.TournamentBuilder desiredNumberOfTables(Integer desiredNumberOfTables) {
            this.desiredNumberOfTables = desiredNumberOfTables;
            return this;
        }

        @Override
        public Tournament build() {
            return new Tournament(this);
        }
    }

    private void createGameTables(int desiredNumberOfTables) {
        List<GameTable> tableList = new ArrayList<>();

        for (int i = 0; i < desiredNumberOfTables; i++) {
            tableList.add(new GameTable());
        }

        log.debug("Created {} game tables", tableList.size());
        this.tablesOfTournament = tableList;
    }

    private void fillTables() {
        int tableIndex;
        for (int playerIndex = 0; playerIndex < this.players.size(); playerIndex++) {
            tableIndex = (this.tablesOfTournament.size() - 1) * (playerIndex / this.tablesOfTournament.size() % 2) + playerIndex % this.tablesOfTournament.size() - playerIndex % this.tablesOfTournament.size() * 2 * (playerIndex / this.tablesOfTournament.size() % 2);
            this.tablesOfTournament.get(tableIndex).addPlayer(this.players.get(playerIndex));
        }
        log.debug("Tables are filled with players");
    }

    private void createRoundsWithMatches(GameTable table) {
        RoundsCreator roundsCreator = new RoundsCreator();
        List<List<Player[]>> schedule = roundsCreator.createRounds(table.getPlayers());

        for (List<Player[]> round: schedule) {
            for (Player[] match: round) {
                table.addMatch(new TablesMatch(match[0], match[1]));
            }
        }
        log.debug("Matches and rounds of the table {} are created", table.getId());
    }
}
