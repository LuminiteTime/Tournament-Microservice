package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatchNotFoundException extends IllegalArgumentException {

    public static final String MESSAGE_TEMPLATE = "Match with id %d does not exist.";

    public MatchNotFoundException(Long tableId) {
        super(String.format(MESSAGE_TEMPLATE, tableId));
        log.warn(super.getMessage());
    }
}
