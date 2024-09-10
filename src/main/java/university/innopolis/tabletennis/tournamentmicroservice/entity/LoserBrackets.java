package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.utils.LoserBracketsCreator;

import java.util.*;

@Setter
@Getter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class LoserBrackets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Player> players = new ArrayList<>();

    @OneToMany
    private List<LoserBracketsMatch> firstMatches = new ArrayList<>();

    @OneToMany
    private List<LoserBracketsMatch> matches = new ArrayList<>();

    @OneToMany
    private Set<LoserBracketsMatch> availableMatches = new HashSet<>();

    @OneToMany
    private Map<Integer, Player> topThree = new HashMap<>();

    public LoserBrackets(int numberOfStartPlayers, Long startLoserMatchIndex, Long startMatchIndex) {
        LoserBracketsCreator loserBracketsCreator = new LoserBracketsCreator(numberOfStartPlayers, startLoserMatchIndex, startMatchIndex);
        this.matches = loserBracketsCreator.getMatches();
        this.firstMatches = loserBracketsCreator.getFirstMatches();
    }
}
