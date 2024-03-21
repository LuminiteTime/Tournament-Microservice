package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.TournamentRepository;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MatchState;

import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    public Match patchMatchState(Long matchId, Optional<PatchMatchRequestBody> matchInfo) {
        Match match = matchRepository
                .findById(matchId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Match with id " + matchId + " does not exist."
                    )
                );

        // Match is already completed, no changes in state needed.
        if (match.getState().equals(MatchState.COMPLETED))
            return match;

        // Match is about to start but an empty request body provided.
        if (match.getState().equals(MatchState.PLAYING) && matchInfo.isEmpty())
            throw new IllegalArgumentException("Match score is not provided.");

        // Switching the state of the match.
        if (match.getState().equals(MatchState.PLAYING)) {
            setMatchIsCompleted(match, matchInfo.get());
        } else {
            setMatchIsBeingPlayed(match);
        }
        matchRepository.save(match);

        return match;
    }

    private void setMatchIsCompleted(Match match, PatchMatchRequestBody matchInfo) {
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
}
