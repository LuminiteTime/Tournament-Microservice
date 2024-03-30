package university.innopolis.tabletennis.tournamentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(name = "Round", description = "Round DTO")
@Getter
@Setter
@NoArgsConstructor
public class RoundDTO {
    @Schema(description = "Matches of the round")
    @NotNull(message = "Matches are not provided")
    private List<MatchDTO> matches;
}
