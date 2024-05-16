package university.innopolis.tabletennis.tournamentmicroservice.utils.validation;

import lombok.extern.slf4j.Slf4j;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

import java.util.Optional;

@Slf4j
public class ValidationUtils {

    private ValidationUtils() {}

    public static MatchInfoValidationResult validateMatchInfo(Match match, Optional<PatchMatchDTO> matchInfo, Long matchId) {
        // Match is already completed, no changes in state needed.
        if (match.getState().equals(MatchState.COMPLETED)) {
            log.info("Match with id {} is already completed", matchId);
            return MatchInfoValidationResult.ALREADY_COMPLETED;
        }

        // Switching the state of the match.
        if (match.getState().equals(MatchState.PLAYING)) {
            // Match is about to start but an empty request body provided.
            if (matchInfo.isEmpty()) {
                log.warn("Match score is not provided");
                throw new IllegalArgumentException("Match score is not provided.");
            } else if (matchInfo.get().getFirstPlayerScore() == null) {
                log.warn("First player score is not provided");
                throw new IllegalArgumentException("First player score is not provided.");
            } else if (matchInfo.get().getSecondPlayerScore() == null) {
                log.warn("Second player score is not provided");
                throw new IllegalArgumentException("Second player score is not provided.");
            }
            return MatchInfoValidationResult.READY_TO_COMPLETE;
        }
        return MatchInfoValidationResult.READY_TO_START;
    }
}