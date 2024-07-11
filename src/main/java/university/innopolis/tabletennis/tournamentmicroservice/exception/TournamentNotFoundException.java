package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TournamentNotFoundException extends IllegalArgumentException {
    private final String message;
    public TournamentNotFoundException(Long tournamentId) {
        this.message = "Tournament with id " + tournamentId + " does not exist.";
        log.warn(this.message);
    }
}
