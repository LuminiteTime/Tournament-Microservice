package university.innopolis.tabletennis.tournamentmicroservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.exception.BracketsNotFoundException;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
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

    private final LoserBracketsMatchRepository loserBracketsMatchRepository;

    private final WinnerBracketsRepository winnerBracketsRepository;

    private final LoserBracketsRepository loserBracketsRepository;

    public WinnerBrackets createBrackets(List<Player> players) {
        playerRepository.saveAll(players);
        WinnerBrackets newWinnerBrackets = new WinnerBrackets(players);
        winnerBracketsMatchRepository.saveAll(newWinnerBrackets.getMatches());
        winnerBracketsRepository.save(newWinnerBrackets);
        return newWinnerBrackets;
    }

    public LoserBrackets createLoserBrackets(int numberOfStartPlayers, Long startLoserBracketsIndex, Long startMatchIndex) {
        LoserBrackets newLoserBrackets = new LoserBrackets(numberOfStartPlayers, startLoserBracketsIndex, startMatchIndex);
        loserBracketsMatchRepository.saveAll(newLoserBrackets.getMatches());
        loserBracketsRepository.save(newLoserBrackets);
        return newLoserBrackets;
    }

    public Set<WinnerBracketsMatch> getAvailableMatches(Long bracketsId) {
        return winnerBracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        ).getAvailableMatches();
    }

    public WinnerBracketsMatch patchBracketsMatchState(Long bracketsId, Long matchId, Optional<PatchMatchDTO> matchInfo) {
        WinnerBrackets winnerBrackets = winnerBracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        );

        List<WinnerBracketsMatch> matchFoundInBracketsById = winnerBrackets.getMatches().stream()
                .filter(bracketsMatch -> bracketsMatch.getId().equals(matchId))
                .toList();
        if (matchFoundInBracketsById.isEmpty()) {
            throw new IllegalArgumentException("Match with id " + matchId + " is not in brackets with id " + bracketsId
                    + " or does not exist at all.");
        }
        WinnerBracketsMatch match = matchFoundInBracketsById.get(0);

        // TODO: Make one method for BracketsService and for MatchService.
        MatchInfoValidationResult validationResult = ValidationUtils.validateMatchInfo(match, matchInfo, matchId);
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

        WinnerBracketsMatch next = match.getNextMatch();
        if (next != null) {
            Player winner = getWinnerFromMatch(match);
            if (next.getFirstPlayer() == null) {
                next.setFirstPlayer(winner);
            } else {
                next.setSecondPlayer(winner);
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

    public Player getWinnerFromMatch(GeneralMatch match) {
        return match.getFirstPlayerScore() > match.getSecondPlayerScore() ?
                match.getFirstPlayer() :
                match.getSecondPlayer();
    }
}
