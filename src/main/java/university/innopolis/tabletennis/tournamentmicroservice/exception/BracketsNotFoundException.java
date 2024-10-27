package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BracketsNotFoundException extends IllegalArgumentException {

    public static final String MESSAGE_TEMPLATE = "Brackets with id %d does not exist.";

    public BracketsNotFoundException(Long bracketsId) {
        super(String.format(MESSAGE_TEMPLATE, bracketsId));
        log.warn(super.getMessage());
    }
}
