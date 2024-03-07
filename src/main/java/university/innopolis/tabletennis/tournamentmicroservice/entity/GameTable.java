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

    private Integer size;

    private Integer indexInTournament;

    public GameTable() {
        this.playersOfTable = new ArrayList<>();
        this.roundsOfTable = new ArrayList<>();
        this.matchesOfTable = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        this.playersOfTable.add(player);
    }

    public void addMatch(Match match) { this.matchesOfTable.add(match); }
    public void addRound(Round round) { this.roundsOfTable.add(round); }
}
