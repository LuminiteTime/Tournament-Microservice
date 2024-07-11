package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GameTableDTO {
    private Long id;

    @NotNull(message = "Players are not provided")
    private final List<PlayerDTO> players;

    @NotNull(message = "Matches are not provided")
    private final List<MatchDTO> matches;

    @Override
    public String toString() {
        return "GameTableDTO{" +
                "players=" + players +
                ", matches=" + matches +
                '}';
    }
}
