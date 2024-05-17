package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.*;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class BracketsMatch implements GeneralMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Player firstPlayer;

    @ManyToOne
    private Player secondPlayer;

    private Long matchIndex;

    @ManyToOne
    private Player winner;

    private int firstPlayerScore;

    private int secondPlayerScore;

    @ManyToOne
    private BracketsMatch nextMatch;

    private MatchState state;
}