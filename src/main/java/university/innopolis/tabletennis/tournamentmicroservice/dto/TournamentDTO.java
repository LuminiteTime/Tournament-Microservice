package university.innopolis.tabletennis.tournamentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "Tournament", description = "Tournament DTO")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {

    @Schema(description = "Title of the tournament, must be unique", example = "Beginners 15")
    @NotBlank(message = "Title is not provided")
    private String title;

    @Schema(description = "Date of the tournament")
    private LocalDate date;

    @Schema(description = "Players of the tournament")
    @NotNull(message = "Players are not provided")
    private List<PlayerDTO> players;

    @Schema(description = "State of the tournament", example = "PLAYING")
//    @NotNull(message = "State is not provided")
    private TournamentState state;
}
