package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TablesMatch implements GeneralMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player firstPlayer;

    @ManyToOne
    private Player secondPlayer;

    private Integer firstPlayerScore;

    private Integer secondPlayerScore;

    private MatchState state;

    public TablesMatch(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.state = MatchState.NOT_PLAYING;
    }
}
