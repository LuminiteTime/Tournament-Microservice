package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.BracketsCreator;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Brackets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Player> players = new ArrayList<>();

    @OneToMany
    private List<BracketsMatch> firstMatches = new ArrayList<>();

    @OneToMany
    private List<BracketsMatch> matches = new ArrayList<>();

    @OneToMany
    private Set<BracketsMatch> availableMatches = new HashSet<>();

    @OneToMany
    private Map<Integer, Player> topThree = new HashMap<>();

    private TournamentState state = TournamentState.PLAYING;

    public Brackets(List<Player> players) {
        this.players = players;
        BracketsCreator bracketsCreator = new BracketsCreator(players);
        this.matches = bracketsCreator.getMatches();
        this.firstMatches = bracketsCreator.getFirstMatches();
        collectAvailableMatches();
    }

    public void collectAvailableMatches() {
        availableMatches.clear();
        for (BracketsMatch firstMatch: firstMatches) {
            BracketsMatch match = firstMatch;
            while (match != null) {
                if (match.getFirstPlayer() != null && match.getSecondPlayer() != null && match.getState() == MatchState.NOT_PLAYING) {
                    availableMatches.add(match);
                }
                match = match.getNextMatch();
            }
        }
    }

    public BracketsMatch getMatch(Long matchIndex) {
        for (BracketsMatch firstMatch: firstMatches) {
            BracketsMatch matchFound = getMatchFromLeaf(firstMatch, matchIndex);
            if (matchFound != null) {
                return matchFound;
            }
        }
        return null;
    }

    private BracketsMatch getMatchFromLeaf(BracketsMatch match, Long matchIndex) {
        while (match != null) {
            if (Objects.equals(match.getMatchIndex(), matchIndex)) {
                return match;
            }
            match = match.getNextMatch();
        }
        return null;
    }

    public void finish() {
        this.state = TournamentState.FINISHED;
        BracketsMatch match = firstMatches.get(0);
        while (match.getNextMatch() != null) {
            match = match.getNextMatch();
        }
        this.topThree.put(1, match.getWinner());
    }
}