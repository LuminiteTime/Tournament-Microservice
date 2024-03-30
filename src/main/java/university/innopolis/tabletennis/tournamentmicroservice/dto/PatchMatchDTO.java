package university.innopolis.tabletennis.tournamentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "Match Info", description = "Score of the match to mark as completed")
@Getter
@Setter
@NoArgsConstructor
public class PatchMatchDTO {
    @Schema(description = "Score of the first player")
    @NotNull(message = "Score of the first player is not provided")
    @Min(value = 1, message = "Invalid score")
    private Integer firstPlayerScore;

    @Schema(description = "Score of the second player")
    @NotNull(message = "Score of the second player is not provided")
    @Min(value = 1, message = "Invalid score")
    private Integer secondPlayerScore;
}
