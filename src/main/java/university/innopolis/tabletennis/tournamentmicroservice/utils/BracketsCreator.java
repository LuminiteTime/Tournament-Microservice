package university.innopolis.tabletennis.tournamentmicroservice.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.entity.BracketsMatch;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class BracketsCreator {
    private final List<Player> players = new ArrayList<>();

    @Getter
    private List<BracketsMatch> matches = new ArrayList<>();

    @Getter
    private List<BracketsMatch> firstMatches = new ArrayList<>();

    private int numberOfRealPlayers;

    private int numberOfAllPlayers;

    public BracketsCreator(List<Player> players) {
        this.players.addAll(players);
        numberOfRealPlayers = players.size();
        numberOfAllPlayers = numberOfRealPlayers % 8 == 0 ? numberOfRealPlayers : numberOfRealPlayers + 8 - numberOfRealPlayers % 8;

        buildBracketsTree();
    }

    private void buildBracketsTree() {
        long matchIndex = 1L;
        List<BracketsMatch> currentRound = new ArrayList<>();
        for (int i = 0; i < numberOfAllPlayers / 2; i++) {
            BracketsMatch bracketsMatch = new BracketsMatch();

            boolean isRealFirstPlayer = i < numberOfRealPlayers;
            boolean isRealSecondPlayer = numberOfAllPlayers - 1 - i < numberOfRealPlayers;

            if (isRealFirstPlayer) {
                bracketsMatch.setFirstPlayer(players.get(i));
            }
            if (isRealSecondPlayer) {
                bracketsMatch.setSecondPlayer(players.get(numberOfAllPlayers - 1 - i));
            }

            if (!isRealFirstPlayer || !isRealSecondPlayer) {
                bracketsMatch.setState(MatchState.SKIPPED);
            } else {
                bracketsMatch.setState(MatchState.NOT_PLAYING);
            }

            bracketsMatch.setMatchIndex(matchIndex++);

            matches.add(bracketsMatch);
            currentRound.add(bracketsMatch);
            firstMatches.add(bracketsMatch);
        }

        while (currentRound.size() > 1) {
            List<BracketsMatch> nextRound = new ArrayList<>();
            for (int i = 0; i < currentRound.size() / 2; i++) {
                BracketsMatch bracketsMatch = new BracketsMatch();

                bracketsMatch.setMatchIndex(matchIndex++);
                bracketsMatch.setState(MatchState.NOT_PLAYING);

                BracketsMatch leftMatch = currentRound.get(i);
                BracketsMatch rightMatch = currentRound.get(currentRound.size() - 1 - i);

                leftMatch.setNextMatch(bracketsMatch);
                rightMatch.setNextMatch(bracketsMatch);

                if (leftMatch.getState().equals(MatchState.SKIPPED)) {
                    if (leftMatch.getFirstPlayer() == null && leftMatch.getSecondPlayer() != null) {
                        bracketsMatch.setFirstPlayer(leftMatch.getSecondPlayer());
                    } else if (leftMatch.getFirstPlayer() != null && leftMatch.getSecondPlayer() == null) {
                        bracketsMatch.setFirstPlayer(leftMatch.getFirstPlayer());
                    } else {
                        bracketsMatch.setState(MatchState.SKIPPED);
                    }
                }

                if (rightMatch.getState().equals(MatchState.SKIPPED)) {
                    if (rightMatch.getFirstPlayer() == null && rightMatch.getSecondPlayer() != null) {
                        bracketsMatch.setSecondPlayer(rightMatch.getSecondPlayer());
                    } else if (rightMatch.getFirstPlayer() != null && rightMatch.getSecondPlayer() == null) {
                        bracketsMatch.setSecondPlayer(rightMatch.getFirstPlayer());
                    } else {
                        bracketsMatch.setState(MatchState.SKIPPED);
                    }
                }

                matches.add(bracketsMatch);
                nextRound.add(bracketsMatch);
            }
            currentRound = nextRound;
        }
    }

}
