package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnableToFinishTournamentException extends IllegalArgumentException {

    public static final String MESSAGE_TEMPLATE = "Tournament with id %d still have available matches.";

    public UnableToFinishTournamentException(Long bracketsId) {
        super(String.format(MESSAGE_TEMPLATE, bracketsId));
        log.warn(super.getMessage());
    }
}
