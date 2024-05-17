package university.innopolis.tabletennis.tournamentmicroservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Brackets;
import university.innopolis.tabletennis.tournamentmicroservice.entity.BracketsMatch;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.exception.BracketsNotFoundException;
import university.innopolis.tabletennis.tournamentmicroservice.repository.BracketsMatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.BracketsRepository;
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

    private final BracketsMatchRepository bracketsMatchRepository;

    private final BracketsRepository bracketsRepository;

    public Brackets createBrackets(List<Player> players) {
        playerRepository.saveAll(players);
        Brackets newBrackets = new Brackets(players);
        bracketsMatchRepository.saveAll(newBrackets.getMatches());
        bracketsRepository.save(newBrackets);
        return newBrackets;
    }

    public Set<BracketsMatch> getAvailableMatches(Long bracketsId) {
        return bracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        ).getAvailableMatches();
    }

    public BracketsMatch patchBracketsMatchState(Long bracketsId, Long matchIndex, Optional<PatchMatchDTO> matchInfo) {
        Brackets brackets = bracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        );
        if (matchIndex > brackets.getMatches().size()) {
            throw new IllegalArgumentException("Match with index " + matchIndex + " does not exist.");
        }

        BracketsMatch match = brackets.getMatch(matchIndex);

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


        brackets.collectAvailableMatches();

        bracketsMatchRepository.save(match);
        bracketsRepository.save(brackets);

        return match;
    }

    private void setMatchIsCompleted(BracketsMatch match, PatchMatchDTO matchInfo) {
        match.setState(MatchState.COMPLETED);
        match.setFirstPlayerScore(matchInfo.getFirstPlayerScore());
        match.setSecondPlayerScore(matchInfo.getSecondPlayerScore());

        match.setWinner(matchInfo.getFirstPlayerScore() > match.getSecondPlayerScore() ?
                match.getFirstPlayer() : match.getSecondPlayer());

        BracketsMatch next = match.getNextMatch();
        if (next != null) {
            if (next.getFirstPlayer() == null) {
                next.setFirstPlayer(match.getWinner());
            } else {
                next.setSecondPlayer(match.getWinner());
            }
            bracketsMatchRepository.save(next);
        }

        log.info("Match with id {} is completed", match.getId());
    }

    private void setMatchIsBeingPlayed(BracketsMatch match) {
        match.setState(MatchState.PLAYING);
        log.info("Match with id {} has been started", match.getId());
    }

    public List<BracketsMatch> getAllMatches(Long bracketsId) {
        return bracketsRepository.findById(bracketsId).orElseThrow(
                () -> new BracketsNotFoundException(bracketsId)
        ).getMatches();
    }
}
