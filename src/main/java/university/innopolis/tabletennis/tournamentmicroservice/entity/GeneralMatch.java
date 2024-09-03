package university.innopolis.tabletennis.tournamentmicroservice.entity;

import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

public interface GeneralMatch {
    void setState(MatchState state);
    MatchState getState();

    Integer getFirstPlayerScore();
    Integer getSecondPlayerScore();

    Player getFirstPlayer();
    Player getSecondPlayer();
}
