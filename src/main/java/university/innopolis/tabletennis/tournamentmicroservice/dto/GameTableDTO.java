package university.innopolis.tabletennis.tournamentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(name = "Game table", description = "Game table DTO")
@Getter
@Builder
@AllArgsConstructor
public class GameTableDTO {
    @Schema(description = "Players in the game table")
    @NotNull(message = "Players are not provided")
    private final List<PlayerDTO> players;

    @Schema(description = "Rounds of the table")
    @NotNull(message = "Rounds are not provided")
    private final List<RoundDTO> rounds;

    @Schema(description = "Matches of the table")
    @NotNull(message = "Matches are not provided")
    private final List<MatchDTO> matches;
}
