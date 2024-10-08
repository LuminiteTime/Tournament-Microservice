package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.*;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class WinnerBracketsMatch implements GeneralMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player firstPlayer;

    @ManyToOne
    private Player secondPlayer;

    private Long matchIndex;

    private Integer firstPlayerScore;

    private Integer secondPlayerScore;

    @ManyToOne
    private WinnerBracketsMatch nextMatch;

    private MatchState state;
}