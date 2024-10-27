package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatchMatchDTO {
    @NotNull(message = "Score of the first player is not provided")
    @Min(value = 0, message = "Too small score")
    @Max(value = 11, message = "Too big score")
    private Integer firstPlayerScore;

    @NotNull(message = "Score of the second player is not provided")
    @Min(value = 0, message = "Too small score")
    @Max(value = 11, message = "Too big score")
    private Integer secondPlayerScore;
}
