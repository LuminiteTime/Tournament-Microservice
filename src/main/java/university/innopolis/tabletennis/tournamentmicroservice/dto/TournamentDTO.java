package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {

    @NotBlank(message = "Title is not provided")
    private String title;

    private LocalDate date;

    @NotNull(message = "Players are not provided")
    private List<PlayerDTO> players;

//    @NotNull(message = "State is not provided")
    private TournamentState state;
}
