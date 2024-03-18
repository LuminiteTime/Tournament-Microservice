package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.exception.InvalidNumberOfPlayersException;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.TournamentInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<GameTable> tablesOfTournament;

    @OneToMany()
    private List<Player> playersOfTournament;

    // TODO: Мапа Игрок - Играет или нет, убрать isPlaying player.

    public Tournament(TournamentInfo info) {
        this.playersOfTournament = info.getPlayers();
        this.tablesOfTournament = chooseGameTables(this.playersOfTournament.size());
        this.fillTables(this.playersOfTournament);
        for (GameTable gameTable: this.tablesOfTournament) {
            this.fillRounds(gameTable, setTypeOfGameTable(gameTable, gameTable.getSize()));
        }
    }

    private static List<GameTable> chooseGameTables(int numberOfPlayers) {
        List<GameTable> tableList = new ArrayList<>();
        if (numberOfPlayers < 8) {
            GameTable table = new GameTable();
            table.setSize(numberOfPlayers);
            tableList.add(table);
        } else {
            int sizeOfGameTable1 = 0;
            int sizeOfGameTable2 = 0;
            GameTable table1 = new GameTable();
            GameTable table2 = new GameTable();
            switch (numberOfPlayers % 2) {
                case 0:
                    sizeOfGameTable1 = sizeOfGameTable2 = numberOfPlayers / 2;
                    break;
                case 1:
                    sizeOfGameTable1 = (numberOfPlayers + 1) / 2;
                    sizeOfGameTable2 = (numberOfPlayers - 1) / 2;
                    break;
            }
            table1.setSize(sizeOfGameTable1);
            table2.setSize(sizeOfGameTable2);

            tableList.add(table1);
            tableList.add(table2);
        }
        return tableList;
    }

    private static List<List<List<Integer>>> setTypeOfGameTable(GameTable gameTable, int sizeOfGameTable) {
        List<List<List<Integer>>> playersIndexes;
        switch (sizeOfGameTable) {
            case 8:
                playersIndexes = List.of(
                        // Round 1
                        List.of(
                                List.of(0, 7),
                                List.of(1, 6),
                                List.of(2, 5),
                                List.of(3, 4)
                        ),
                        // Round 2
                        List.of(
                                List.of(6, 0),
                                List.of(7, 5),
                                List.of(1, 4),
                                List.of(2, 3)
                        ),
                        // Round 3
                        List.of(
                                List.of(0, 5),
                                List.of(6, 4),
                                List.of(7, 3),
                                List.of(1, 2)
                        ),
                        // Round 4
                        List.of(
                                List.of(4, 0),
                                List.of(5, 3),
                                List.of(6, 2),
                                List.of(7, 1)
                        ),
                        // Round 5
                        List.of(
                                List.of(0, 3),
                                List.of(4, 2),
                                List.of(5, 1),
                                List.of(6, 7)
                        ),
                        // Round 6
                        List.of(
                                List.of(2, 0),
                                List.of(3, 1),
                                List.of(4, 7),
                                List.of(5, 6)
                        ),
                        // Round 7
                        List.of(
                                List.of(0, 1),
                                List.of(2, 7),
                                List.of(3, 6),
                                List.of(4, 5)
                        )
                );
                break;
            case 7:
                playersIndexes = List.of(
                        // Round 1
                        List.of(
                                List.of(1, 6),
                                List.of(2, 5),
                                List.of(3, 4)
                        ),
                        // Round 2
                        List.of(
                                List.of(6, 0),
                                List.of(1, 4),
                                List.of(2, 3)
                        ),
                        // Round 3
                        List.of(
                                List.of(0, 5),
                                List.of(6, 4),
                                List.of(1, 2)
                        ),
                        // Round 4
                        List.of(
                                List.of(4, 0),
                                List.of(5, 3),
                                List.of(6, 2)
                        ),
                        // Round 5
                        List.of(
                                List.of(0, 3),
                                List.of(4, 2),
                                List.of(5, 1)
                        ),
                        // Round 6
                        List.of(
                                List.of(2, 0),
                                List.of(3, 1),
                                List.of(5, 6)
                        ),
                        // Round 7
                        List.of(
                                List.of(0, 1),
                                List.of(3, 6),
                                List.of(4, 5)
                        )
                );
                break;
            case 6:
                playersIndexes = List.of(
                        // Round 1
                        List.of(
                                List.of(0, 5),
                                List.of(1, 4),
                                List.of(2, 3)
                        ),
                        // Round 2
                        List.of(
                                List.of(4, 0),
                                List.of(5, 3),
                                List.of(1, 2)
                        ),
                        // Round 3
                        List.of(
                                List.of(0, 3),
                                List.of(4, 2),
                                List.of(5, 1)
                        ),
                        // Round 4
                        List.of(
                                List.of(2, 0),
                                List.of(3, 1),
                                List.of(4, 5)
                        ),
                        // Round 5
                        List.of(
                                List.of(0, 1),
                                List.of(2, 5),
                                List.of(3, 4)
                        )
                );
                break;
            case 5:
                playersIndexes = List.of(
                        // Round 1
                        List.of(
                                List.of(1, 4),
                                List.of(2, 3)
                        ),
                        // Round 2
                        List.of(
                                List.of(4, 0),
                                List.of(1, 2)
                        ),
                        // Round 3
                        List.of(
                                List.of(0, 3),
                                List.of(4, 2)
                        ),
                        // Round 4
                        List.of(
                                List.of(2, 0),
                                List.of(3, 1)
                        ),
                        // Round 5
                        List.of(
                                List.of(0, 1),
                                List.of(3, 4)
                        )
                );
                break;
            case 4:
                playersIndexes = List.of(
                        // Round 1
                        List.of(
                                List.of(0, 3),
                                List.of(1, 2)
                        ),
                        // Round 2
                        List.of(
                                List.of(0, 2),
                                List.of(1, 3)
                        ),
                        // Round 3
                        List.of(
                                List.of(0, 1),
                                List.of(2, 3)
                        )
                );
                break;
            default:
                playersIndexes = new ArrayList<>();
        }
        return playersIndexes;
    }

    public void fillTables(List<Player> allPlayers) {
        int tableIndex = 0;
        for (int i = 0; i < allPlayers.size(); i++) {
            Player player = allPlayers.get(i);
            this.tablesOfTournament.get(tableIndex).addPlayer(player);
            tableIndex = (tableIndex + 1) % this.tablesOfTournament.size();
        }
    }

    public void fillRounds(GameTable table, List<List<List<Integer>>> playersIndexes) {
        for (List<List<Integer>> playersIndex: playersIndexes) {
            Round roundToAdd = new Round();
            for (List<Integer> match: playersIndex) {
                Match matchToAdd = new Match(
                        table.getPlayersOfTable().get(match.get(0)),
                        table.getPlayersOfTable().get(match.get(1))
                );
                roundToAdd.addMatch(matchToAdd);
                table.addMatch(matchToAdd);
            }
            table.addRound(roundToAdd);
        }
    }
}
