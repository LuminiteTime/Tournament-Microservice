package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
//    @JoinColumn(name = "first_player_id", nullable = false)
    private Player firstPlayer;

    @ManyToOne
//    @JoinColumn(name = "second_player_id", nullable = false)
    private Player secondPlayer;

    private Integer firstPlayerScore;

    private Integer secondPlayerScore;

//    @ManyToOne
//    @JoinColumn(name = "tournament", nullable = false)
//    private Tournament tournament;

    public Match(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayerScore = 0;
        this.secondPlayerScore = 0;
    }
}
