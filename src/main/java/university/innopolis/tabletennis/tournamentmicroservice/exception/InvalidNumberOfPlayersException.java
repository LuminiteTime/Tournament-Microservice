package university.innopolis.tabletennis.tournamentmicroservice.exception;

public class InvalidNumberOfPlayersException extends Exception {

    public InvalidNumberOfPlayersException() {
    }

    @Override
    public String getMessage() {
        return "Number of players is either too small or cannot be distributed into existing types of tables.";
    }
}
