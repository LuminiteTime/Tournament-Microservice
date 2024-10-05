package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    private Long id;

    @NotNull(message = "First player id is not provided")
    @Min(value = 1, message = "Invalid id format")
    private Long firstPlayerId;

    @NotNull(message = "Second player id is not provided")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchDTO matchDTO = (MatchDTO) o;
        return Objects.equals(getId(), matchDTO.getId()) &&
                Objects.equals(getFirstPlayerId(), matchDTO.getFirstPlayerId()) &&
                Objects.equals(getSecondPlayerId(), matchDTO.getSecondPlayerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstPlayerId(), getSecondPlayerId());
    }
}
