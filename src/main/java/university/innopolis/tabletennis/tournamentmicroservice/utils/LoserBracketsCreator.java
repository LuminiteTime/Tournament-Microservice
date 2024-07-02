package university.innopolis.tabletennis.tournamentmicroservice.utils;

import lombok.Getter;
import university.innopolis.tabletennis.tournamentmicroservice.entity.LoserBracketsMatch;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

import java.util.ArrayList;
import java.util.List;

public class LoserBracketsCreator {
    int numberOfAllPlayers;

    Long startMatchIndex;

    Long startLoserBracketsIndex;

    @Getter
    private List<LoserBracketsMatch> matches = new ArrayList<>();

    @Getter
    private List<LoserBracketsMatch> firstMatches = new ArrayList<>();

    public LoserBracketsCreator(int numberOfAllPlayers, Long startLoserBracketsIndex, Long startMatchIndex) {
        this.numberOfAllPlayers = numberOfAllPlayers;
        this.startMatchIndex = startMatchIndex;
        this.startLoserBracketsIndex = startLoserBracketsIndex;

        buildBracketsTree();
    }

    private void buildBracketsTree() {
        List<LoserBracketsMatch> currentRound = new ArrayList<>();

        boolean createMatchFromWinnerBracketsMatchIndex = true;
        for (int i = 0; i < numberOfAllPlayers / 2; i++) {
            LoserBracketsMatch loserBracketsMatch = new LoserBracketsMatch();

            if (createMatchFromWinnerBracketsMatchIndex) {
                loserBracketsMatch.setState(MatchState.SKIPPED);
            }

            matches.add(loserBracketsMatch);
            currentRound.add(loserBracketsMatch);
            firstMatches.add(loserBracketsMatch);

            createMatchFromWinnerBracketsMatchIndex = !createMatchFromWinnerBracketsMatchIndex;
        }

        int roundIndex = 1;
        while (currentRound.size() > 1) {
            List<LoserBracketsMatch> nextRound = new ArrayList<>();
            for (int i = 0; i < currentRound.size() / 2; i++) {
                LoserBracketsMatch loserBracketsMatch = new LoserBracketsMatch();

                loserBracketsMatch.setState(MatchState.NOT_PLAYING);


                // * SETTING MATCH INDEXES FOR PLAYERS TO COME
                LoserBracketsMatch leftMatch;
                LoserBracketsMatch rightMatch;

                leftMatch = currentRound.get(i);
                leftMatch.setNextMatch(loserBracketsMatch);
                leftMatch.setMatchIndex(startMatchIndex++);

                rightMatch = currentRound.get(currentRound.size() - 1 - i);
                if (!rightMatch.getState().equals(MatchState.SKIPPED)) {
                    rightMatch.setNextMatch(loserBracketsMatch);
                    rightMatch.setMatchIndex(startMatchIndex++);
                } else {
                    loserBracketsMatch.setFromSecondPlayerMatchIndex();
                }

                if (roundIndex % 2 != 0) {
                    if (roundIndex == 1) {
                        leftMatch.setFromFirstPlayerMatchIndex(startLoserBracketsIndex++);
                        leftMatch.setFromSecondPlayerMatchIndex(startLoserBracketsIndex++);
                    }

                    loserBracketsMatch.setFromSecondPlayerMatchIndex(startLoserBracketsIndex++);
                } else {
                    rightMatch = currentRound.get(currentRound.size() - 1 - i);
                    rightMatch.setNextMatch(loserBracketsMatch);
                    rightMatch.setMatchIndex(startMatchIndex++);
                }

                // * SETTING MATCH INDEXES FOR PLAYERS TO COME

//                System.out.println(roundIndex + " : " + leftMatch.getMatchIndex());

                matches.add(loserBracketsMatch);
                nextRound.add(loserBracketsMatch);

                loserBracketsMatch.setRoundIndex(roundIndex);
            }
            currentRound = nextRound;
            roundIndex++;
        }
        currentRound.get(0).setMatchIndex(startMatchIndex);
    }
}
