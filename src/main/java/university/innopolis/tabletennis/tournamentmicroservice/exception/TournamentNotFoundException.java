package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TournamentNotFoundException extends IllegalArgumentException {

    public static final String MESSAGE_TEMPLATE = "Tournament with id %d does not exist.";

    public TournamentNotFoundException(Long tournamentId) {
        super(String.format(MESSAGE_TEMPLATE, tournamentId));
        log.warn(super.getMessage());
    }
}
