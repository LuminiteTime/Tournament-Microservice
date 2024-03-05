package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class GameTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Player> playersOfTable;

    @OneToMany
    private List<Round> roundsOfTable;

    @OneToMany
    private List<Match> matchesOfTable;

    public GameTable() {
        this.playersOfTable = new ArrayList<>();
        this.roundsOfTable = new ArrayList<>();
        this.matchesOfTable = new ArrayList<>();
    }

    public void fillRounds() {
        List<List<List<Integer>>> playersIndexes;
        switch (this.playersOfTable.size()) {
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
            default:
                playersIndexes = new ArrayList<>();
        }
        for (List<List<Integer>> roundIndexes: playersIndexes) {
            Round roundToAdd = new Round();
            for (List<Integer> match: roundIndexes) {
                Match matchToAdd = new Match(
                        this.playersOfTable.get(match.get(0)),
                        this.playersOfTable.get(match.get(1))
                );
                roundToAdd.addMatch(matchToAdd);
                this.matchesOfTable.add(matchToAdd);
            }
            this.roundsOfTable.add(roundToAdd);
        }
    }

    public void addPlayer(Player player) {
        this.playersOfTable.add(player);
    }
}
