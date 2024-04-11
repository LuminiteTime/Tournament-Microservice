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
    @NotNull(message = "Players are not provided")
    private final List<PlayerDTO> players;

    @NotNull(message = "Rounds are not provided")
    private final List<RoundDTO> rounds;

    @NotNull(message = "Matches are not provided")
    private final List<MatchDTO> matches;
}
