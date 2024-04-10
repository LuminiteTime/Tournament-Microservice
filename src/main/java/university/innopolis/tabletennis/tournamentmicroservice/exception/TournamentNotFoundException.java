package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.Getter;

@Getter
public class TournamentNotFoundException extends IllegalArgumentException {
    String message;
    public TournamentNotFoundException(Long tournamentId) {
        this.message = "Tournament with id " + tournamentId + " does not exist.";
    }
}
