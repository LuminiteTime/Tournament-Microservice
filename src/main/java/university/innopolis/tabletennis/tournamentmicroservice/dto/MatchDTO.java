package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    @NotNull(message = "Id is not provided")
    @Min(value = 1, message = "Invalid id format")
    private Long firstPlayerId;

    @NotNull(message = "Id is not provided")
    @Min(value = 1, message = "Invalid id format")
    private Long secondPlayerId;

    @NotNull(message = "Score for the first player is not provided")
    @Min(value = 0, message = "Too small score")
    @Max(value = 11, message = "Too big score")
    private Integer firstPlayerScore;

    @NotNull(message = "Score for the second player is not provided")
    @Min(value = 0, message = "Too small score")
    @Max(value = 11, message = "Too big score")
    private Integer secondPlayerScore;

    @NotNull(message = "State of the match is not provided")
    private MatchState state;

    @Override
    public String toString() {
        return "MatchDTO{" +
                "firstPlayerId=" + firstPlayerId +
                ", secondPlayerId=" + secondPlayerId +
                ", firstPlayerScore=" + firstPlayerScore +
                ", secondPlayerScore=" + secondPlayerScore +
                ", state=" + state +
                '}';
    }
}
