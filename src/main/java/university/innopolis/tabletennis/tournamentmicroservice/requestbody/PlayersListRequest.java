package university.innopolis.tabletennis.tournamentmicroservice.requestbody;

import lombok.Getter;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlayersListRequest {
    private List<Player> playersList;

    public PlayersListRequest() {
        this.playersList = new ArrayList<>();
    }
}
