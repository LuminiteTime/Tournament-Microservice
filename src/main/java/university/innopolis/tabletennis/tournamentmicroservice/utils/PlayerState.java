package university.innopolis.tabletennis.tournamentmicroservice.utils;

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
