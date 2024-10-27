package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameTableNotFoundException extends IllegalArgumentException {

    public static final String MESSAGE_TEMPLATE = "Game table with id %d does not exist.";

    public GameTableNotFoundException(Long tableId) {
        super(String.format(MESSAGE_TEMPLATE, tableId));
        log.warn(super.getMessage());
    }
}
