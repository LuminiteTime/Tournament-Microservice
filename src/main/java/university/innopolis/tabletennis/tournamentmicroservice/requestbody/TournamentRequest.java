package university.innopolis.tabletennis.tournamentmicroservice.requestbody;

import lombok.Getter;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TournamentRequest {
    private List<Player> playersList;

    public TournamentRequest() {
        this.playersList = new ArrayList<>();
    }
}
