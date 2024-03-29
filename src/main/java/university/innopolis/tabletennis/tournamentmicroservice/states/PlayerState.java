package university.innopolis.tabletennis.tournamentmicroservice.states;

public enum PlayerState {
    FREE (false),
    PLAYING (true);

    private final Boolean isBusy;

    PlayerState(Boolean isBusy) {
        this.isBusy = isBusy;
    }

    public Boolean isBusy() {
        return this.isBusy;
    }
}
