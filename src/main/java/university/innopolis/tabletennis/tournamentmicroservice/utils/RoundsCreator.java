package university.innopolis.tabletennis.tournamentmicroservice.utils;


import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;

import java.util.*;

@NoArgsConstructor
public class RoundsCreator {
    private int n;
    private int half;
    private List<Integer> players;


    public List<List<Player[]>> prepareRounds(List<Player> playersToSchedule) {
        n = playersToSchedule.size();
        if (n % 2 == 1) {
            n += 1;
        }
        players = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            players.add(i);
        }
        half = n / 2;
        List<List<Integer[]>> scheduleIndexes = new ArrayList<>();
        scheduleIndexes.add(createRound(1));
        for (int roundIndex = 2; roundIndex < n; roundIndex++) {
            Integer fixedPlayer = players.remove(n - 1);
            players.addAll(new ArrayList<>(players.subList(0, half)));
            List<Integer> temp = new ArrayList<>(players.subList(half, n - 1));
            for (int i = 0; i < half - 1; i++) {
                players.set(i, temp.get(i));
            }
            players.subList(half - 1, n - 1).clear();
            players.add(fixedPlayer);
            scheduleIndexes.add(createRound(roundIndex));
        }
        List<List<Player[]>> schedule = new ArrayList<>();
        for (List<Integer[]> roundIndexes: scheduleIndexes) {
            List<Player[]> round = new ArrayList<>();
            for (Integer[] matchIndexes: roundIndexes) {
                if (matchIndexes[0] == playersToSchedule.size() || matchIndexes[1] == playersToSchedule.size()) {
                    continue;
                }
                round.add(new Player[]{playersToSchedule.get(matchIndexes[0]), playersToSchedule.get(matchIndexes[1])});
            }
            schedule.add(round);
        }
        return schedule;
    }

    private List<Integer[]> createRound(int roundNumber) {
        List<Integer[]> round = new ArrayList<>();
        int startIndex;
        if (roundNumber % 2 == 1) {
            startIndex = 0;
        } else {
            startIndex = 1;
            round.add(new Integer[]{this.players.get(n - 1), this.players.get(0)});
        }
        for (int i = startIndex; i < half; i++) {
            round.add(new Integer[]{this.players.get(i), this.players.get(n - 1 - i)});
        }
        return round;
    }
}