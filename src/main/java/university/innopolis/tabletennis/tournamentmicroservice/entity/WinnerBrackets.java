package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.WinnerBracketsCreator;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class WinnerBrackets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Player> players = new ArrayList<>();

    @OneToMany
    private List<WinnerBracketsMatch> firstMatches = new ArrayList<>();

    @OneToMany
    private List<WinnerBracketsMatch> matches = new ArrayList<>();

    @OneToMany
    private Set<WinnerBracketsMatch> availableMatches = new HashSet<>();

    private TournamentState state = TournamentState.PLAYING;

    public WinnerBrackets(List<Player> players) {
        this.players = players;
        WinnerBracketsCreator winnerBracketsCreator = new WinnerBracketsCreator(players);
        this.matches = winnerBracketsCreator.getMatches();
        this.firstMatches = winnerBracketsCreator.getFirstMatches();
        collectAvailableMatches();
    }

    public void collectAvailableMatches() {
        availableMatches.clear();
        for (WinnerBracketsMatch firstMatch: firstMatches) {
            WinnerBracketsMatch match = firstMatch;
            while (match != null) {
                if (match.getFirstPlayer() != null && match.getSecondPlayer() != null && match.getState() == MatchState.NOT_PLAYING) {
                    availableMatches.add(match);
                }
                match = match.getNextMatch();
            }
        }
    }

    public void finish() {
        this.state = TournamentState.FINISHED;
    }
}