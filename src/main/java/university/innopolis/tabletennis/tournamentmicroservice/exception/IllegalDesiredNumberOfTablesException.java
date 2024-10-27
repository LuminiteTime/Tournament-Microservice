package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IllegalDesiredNumberOfTablesException extends IllegalArgumentException {

    public static final String MESSAGE_TEMPLATE = "Desired number of tables must be less than the number of players minus 1";

    public IllegalDesiredNumberOfTablesException() {
        super(MESSAGE_TEMPLATE);
        log.warn(super.getMessage());
    }
}
