package university.innopolis.tabletennis.tournamentmicroservice.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.entity.WinnerBracketsMatch;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class WinnerBracketsCreator {
    private final List<Player> players = new ArrayList<>();

    @Getter
    private List<WinnerBracketsMatch> matches = new ArrayList<>();

    @Getter
    private List<WinnerBracketsMatch> firstMatches = new ArrayList<>();

    private int numberOfRealPlayers;

    private int numberOfAllPlayers;

    public WinnerBracketsCreator(List<Player> players) {
        this.players.addAll(players);
        numberOfRealPlayers = players.size();
        numberOfAllPlayers = numberOfRealPlayers % 8 == 0 ? numberOfRealPlayers : numberOfRealPlayers + 8 - numberOfRealPlayers % 8;

        buildBracketsTree();
    }

    private void buildBracketsTree() {
        long matchIndex = 1L;
        List<WinnerBracketsMatch> currentRound = new ArrayList<>();


        for (int i = 0; i < numberOfAllPlayers / 2; i++) {
            WinnerBracketsMatch winnerBracketsMatch = createLeafWinnerBracketsMatch(i);

            matches.add(winnerBracketsMatch);
            currentRound.add(winnerBracketsMatch);
            firstMatches.add(winnerBracketsMatch);
        }

        while (currentRound.size() > 1) {
            List<WinnerBracketsMatch> nextRound = new ArrayList<>();
            for (int i = 0; i < currentRound.size() / 2; i++) {
                WinnerBracketsMatch winnerBracketsMatch = new WinnerBracketsMatch();

                winnerBracketsMatch.setState(MatchState.NOT_PLAYING);

                WinnerBracketsMatch leftMatch = currentRound.get(i);
                WinnerBracketsMatch rightMatch = currentRound.get(currentRound.size() - 1 - i);

                leftMatch.setNextMatch(winnerBracketsMatch);
                rightMatch.setNextMatch(winnerBracketsMatch);

                leftMatch.setMatchIndex(matchIndex++);
                rightMatch.setMatchIndex(matchIndex++);

                if (leftMatch.getState().equals(MatchState.SKIPPED)) {
                    if (leftMatch.getFirstPlayer() == null && leftMatch.getSecondPlayer() != null) {
                        winnerBracketsMatch.setFirstPlayer(leftMatch.getSecondPlayer());
                    } else if (leftMatch.getFirstPlayer() != null && leftMatch.getSecondPlayer() == null) {
                        winnerBracketsMatch.setFirstPlayer(leftMatch.getFirstPlayer());
                    } else {
                        winnerBracketsMatch.setState(MatchState.SKIPPED);
                    }
                }

                if (rightMatch.getState().equals(MatchState.SKIPPED)) {
                    if (rightMatch.getFirstPlayer() == null && rightMatch.getSecondPlayer() != null) {
                        winnerBracketsMatch.setSecondPlayer(rightMatch.getSecondPlayer());
                    } else if (rightMatch.getFirstPlayer() != null && rightMatch.getSecondPlayer() == null) {
                        winnerBracketsMatch.setSecondPlayer(rightMatch.getFirstPlayer());
                    } else {
                        winnerBracketsMatch.setState(MatchState.SKIPPED);
                    }
                }

                matches.add(winnerBracketsMatch);
                nextRound.add(winnerBracketsMatch);
            }
            currentRound = nextRound;
        }
        currentRound.get(0).setMatchIndex(matchIndex);
    }

    WinnerBracketsMatch createLeafWinnerBracketsMatch(int i) {
        WinnerBracketsMatch winnerBracketsMatch = new WinnerBracketsMatch();

        boolean isRealFirstPlayer = i < numberOfRealPlayers;
        boolean isRealSecondPlayer = numberOfAllPlayers - 1 - i < numberOfRealPlayers;

        if (isRealFirstPlayer) {
            winnerBracketsMatch.setFirstPlayer(players.get(i));
        }
        if (isRealSecondPlayer) {
            winnerBracketsMatch.setSecondPlayer(players.get(numberOfAllPlayers - 1 - i));
        }

        if (!isRealFirstPlayer || !isRealSecondPlayer) {
            winnerBracketsMatch.setState(MatchState.SKIPPED);
        } else {
            winnerBracketsMatch.setState(MatchState.NOT_PLAYING);
        }

        return winnerBracketsMatch;
    }

    // TODO: Fill leaves players.

}
