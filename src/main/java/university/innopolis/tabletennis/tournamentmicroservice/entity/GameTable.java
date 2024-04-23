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
    private List<Player> players;

    @OneToMany
    private List<Round> rounds;

    @OneToMany
    private List<Match> matches;

    public GameTable() {
        this.players = new ArrayList<>();
        this.rounds = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addMatch(Match match) { this.matches.add(match); }
    public void addRound(Round round) { this.rounds.add(round); }
}
