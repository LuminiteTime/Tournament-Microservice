package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class LoserBracketsMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long fromFirstPlayerMatchIndex;

    private Long fromSecondPlayerMatchIndex;

    private Long matchIndex;

    @ManyToOne
    private Player firstPlayer;

    @ManyToOne
    private Player secondPlayer;

    private Integer firstPlayerScore;

    private Integer secondPlayerScore;

    @ManyToOne
    private LoserBracketsMatch nextMatch;

    private MatchState state;

    private int roundIndex;
}
