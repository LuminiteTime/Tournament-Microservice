package university.innopolis.tabletennis.tournamentmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BracketsMatchDTO {
    private Long id;
    private Long firstPlayerId;
    private Long secondPlayerId;
    private Integer firstPlayerScore;
    private Integer secondPlayerScore;
    private Long matchIndex;
    private Long winnerId;
    private Long nextMatchIndex;
    private MatchState state;
}
