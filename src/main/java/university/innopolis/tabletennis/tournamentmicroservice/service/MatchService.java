package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Tournament;
import university.innopolis.tabletennis.tournamentmicroservice.repository.MatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.TournamentRepository;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;

import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    public Match patchMatchState(Long tournamentId, Long matchId, Optional<PatchMatchRequestBody> matchInfo) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");

        Optional<Match> matchToPatch = matchRepository.findById(matchId);
        if (matchToPatch.isEmpty()) throw new IllegalArgumentException("Match with id " + matchId + " does not exist.");

        Match match = matchToPatch.get();
        if (match.getIsCompleted()) return match;

        if (match.getIsBeingPlayed() && matchInfo.isEmpty()) throw new IllegalArgumentException("Match score is not provided.");

        if (!match.getIsBeingPlayed()) {
            setMatchIsBeingPlayed(match);
        } else {
            setMatchIsCompleted(match, matchInfo.get());
        }
        matchRepository.save(match);

        return match;
    }

    private void setMatchIsCompleted(Match match, PatchMatchRequestBody matchInfo) {
        match.setIsCompleted(true);
        match.setIsBeingPlayed(false);

        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();
//        firstPlayer.setIsPlaying(false);
//        secondPlayer.setIsPlaying(false);
        playerRepository.save(firstPlayer);
        playerRepository.save(secondPlayer);

        match.setFirstPlayerScore(matchInfo.getFirstPlayerScore());
        match.setSecondPlayerScore(matchInfo.getSecondPlayerScore());
    }

    private void setMatchIsBeingPlayed(Match match) {
        match.setIsBeingPlayed(true);

        Player firstPlayer = match.getFirstPlayer();
        Player secondPlayer = match.getSecondPlayer();
//        firstPlayer.setIsPlaying(true);
//        secondPlayer.setIsPlaying(true);
        playerRepository.save(firstPlayer);
        playerRepository.save(secondPlayer);
    }
}
