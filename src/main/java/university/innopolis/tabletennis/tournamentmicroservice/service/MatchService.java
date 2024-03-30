package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.GameTable;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.repository.GameTableRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;
import university.innopolis.tabletennis.tournamentmicroservice.states.PlayerState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils;

import java.util.*;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameTableRepository gameTableRepository;

    public MatchDTO patchMatchState(Long matchId, Optional<PatchMatchDTO> matchInfo) {
        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Match with id " + matchId + " does not exist."
                        )
                );

        // Match is already completed, no changes in state needed.
        if (match.getState().equals(MatchState.COMPLETED))
            return MappingUtils.mapToMatchDTO(match);

        // Match is about to start but an empty request body provided.
        if (match.getState().equals(MatchState.PLAYING)) {
            if (matchInfo.isEmpty()) {
                throw new IllegalArgumentException("Match score is not provided.");
            } else if (matchInfo.get().getFirstPlayerScore() == null) {
                throw new IllegalArgumentException("First player score is not provided.");
            } else if (matchInfo.get().getSecondPlayerScore() == null) {
                throw new IllegalArgumentException("Second player score is not provided.");
            }
        }

        // Switching the state of the match.
        if (match.getState().equals(MatchState.PLAYING)) {
            setMatchIsCompleted(match, matchInfo.get());
        } else {
            setMatchIsBeingPlayed(match);
        }
        matchRepository.save(match);

        return MappingUtils.mapToMatchDTO(match);
    }

    private void setMatchIsCompleted(Match match, PatchMatchDTO matchInfo) {
        match.setState(MatchState.COMPLETED);

        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();

        playerRepository.save(firstPlayer);
        playerRepository.save(secondPlayer);

        match.setFirstPlayerScore(matchInfo.getFirstPlayerScore());
        match.setSecondPlayerScore(matchInfo.getSecondPlayerScore());
    }

    private void setMatchIsBeingPlayed(Match match) {
        match.setState(MatchState.PLAYING);

        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();

        playerRepository.save(firstPlayer);
        playerRepository.save(secondPlayer);
    }

    public List<MatchDTO> retrieveAvailableMatches(Long tableId) {
        GameTable gameTable = gameTableRepository
                .findById(tableId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Game table with id " + tableId + " does not exist."
                        )
                );

        List<Match> availableMatches = new ArrayList<>();
        Map<Player, PlayerState> tempBusy = new HashMap<>();

        for (Player player: gameTable.getPlayers()) {
            tempBusy.put(player, PlayerState.FREE);
        }

        for (Match match: gameTable.getMatches()) {
            if (!match.getState().equals(MatchState.NOT_PLAYING) ||
                    tempBusy.get(match.getFirstPlayer()).isBusy() ||
                    tempBusy.get(match.getSecondPlayer()).isBusy())
                continue;

            availableMatches.add(match);
            tempBusy.put(match.getFirstPlayer(), PlayerState.PLAYING);
            tempBusy.put(match.getSecondPlayer(), PlayerState.PLAYING);
        }
        return availableMatches.stream()
                .map(MappingUtils::mapToMatchDTO)
                .toList();
    }
}
