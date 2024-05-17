package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BracketsNotFoundException extends IllegalArgumentException {
    private final String message;
    public BracketsNotFoundException(Long bracketsId) {
        this.message = "Brackets with id " + bracketsId + " does not exist.";
        log.warn(this.message);
    }
}
