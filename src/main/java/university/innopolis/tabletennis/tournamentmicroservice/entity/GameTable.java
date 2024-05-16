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
    private List<TablesMatch> tablesMatches;

    public GameTable() {
        this.players = new ArrayList<>();
        this.tablesMatches = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addMatch(TablesMatch tablesMatch) { this.tablesMatches.add(tablesMatch); }
}
