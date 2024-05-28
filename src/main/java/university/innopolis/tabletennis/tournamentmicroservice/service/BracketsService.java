package university.innopolis.tabletennis.tournamentmicroservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.WinnerBrackets;
import university.innopolis.tabletennis.tournamentmicroservice.entity.WinnerBracketsMatch;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.exception.BracketsNotFoundException;
import university.innopolis.tabletennis.tournamentmicroservice.repository.WinnerBracketsMatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.WinnerBracketsRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.PlayerRepository;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.validation.MatchInfoValidationResult;
import university.innopolis.tabletennis.tournamentmicroservice.utils.validation.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class BracketsService {

    private final PlayerRepository playerRepository;

    private final WinnerBracketsMatchRepository winnerBracketsMatchRepository;

    private final WinnerBracketsRepository winnerBracketsRepository;

    public WinnerBrackets createBrackets(List<Player> players) {
        playerRepository.saveAll(players);
        WinnerBrackets newWinnerBrackets = new WinnerBrackets(players);
        winnerBracketsMatchRepository.saveAll(newWinnerBrackets.getMatches());
        winnerBracketsRepository.save(newWinnerBrackets);
        return newWinnerBrackets;
    }

    public Set<WinnerBracketsMatch> getAvailableMatches(Long bracketsId) {
        return winnerBracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        ).getAvailableMatches();
    }

    public WinnerBracketsMatch patchBracketsMatchState(Long bracketsId, Long matchIndex, Optional<PatchMatchDTO> matchInfo) {
        WinnerBrackets winnerBrackets = winnerBracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        );
        if (matchIndex > winnerBrackets.getMatches().size()) {
            throw new IllegalArgumentException("Match with index " + matchIndex + " does not exist.");
        }

        WinnerBracketsMatch match = winnerBrackets.getMatch(matchIndex);

        // TODO: Make one method for BracketsService and for MatchService.
        MatchInfoValidationResult validationResult = ValidationUtils.validateMatchInfo(match, matchInfo, matchIndex);
        switch (validationResult) {
            case ALREADY_COMPLETED:
                return match;
            case READY_TO_START:
                setMatchIsBeingPlayed(match);
                break;
            case READY_TO_COMPLETE:
                setMatchIsCompleted(match, matchInfo.get());
                break;
            default:
                log.error("Unknown error occurred while patching the match");
                throw new IllegalArgumentException("Unknown error occurred while patching the match.");
        }


        winnerBrackets.collectAvailableMatches();

        winnerBracketsMatchRepository.save(match);
        winnerBracketsRepository.save(winnerBrackets);

        return match;
    }

    private void setMatchIsCompleted(WinnerBracketsMatch match, PatchMatchDTO matchInfo) {
        match.setState(MatchState.COMPLETED);
        match.setFirstPlayerScore(matchInfo.getFirstPlayerScore());
        match.setSecondPlayerScore(matchInfo.getSecondPlayerScore());

        match.setWinner(matchInfo.getFirstPlayerScore() > match.getSecondPlayerScore() ?
                match.getFirstPlayer() : match.getSecondPlayer());

        WinnerBracketsMatch next = match.getNextMatch();
        if (next != null) {
            if (next.getFirstPlayer() == null) {
                next.setFirstPlayer(match.getWinner());
            } else {
                next.setSecondPlayer(match.getWinner());
            }
            winnerBracketsMatchRepository.save(next);
        }

        log.info("Match with id {} is completed", match.getId());
    }

    private void setMatchIsBeingPlayed(WinnerBracketsMatch match) {
        match.setState(MatchState.PLAYING);
        log.info("Match with id {} has been started", match.getId());
    }

    public List<WinnerBracketsMatch> getAllMatches(Long bracketsId) {
        return winnerBracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        ).getMatches();
    }

    public WinnerBrackets finishBrackets(Long bracketsId) {
        WinnerBrackets winnerBrackets = winnerBracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        );
        winnerBrackets.finish();
        return winnerBrackets;
    }
}
