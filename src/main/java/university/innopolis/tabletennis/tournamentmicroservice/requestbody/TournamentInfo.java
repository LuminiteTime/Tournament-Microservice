package university.innopolis.tabletennis.tournamentmicroservice.requestbody;

import lombok.Getter;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TournamentInfo {
    private String title;
    private LocalDate date;
    private List<Player> players;

    public TournamentInfo() {
        this.players = new ArrayList<>();
    }
}
