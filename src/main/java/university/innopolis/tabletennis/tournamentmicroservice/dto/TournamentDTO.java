package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TournamentDTO {
    private Long id;

    @NotBlank(message = "Title is not provided")
    private String title;

    private LocalDate date;

    @NotNull(message = "Players are not provided")
    private List<PlayerDTO> players;

    @NotNull(message = "Desired number of tables is not provided")
    @Min(value = 1, message = "Amount of tables must be at least 1")
    private Integer amountOfTables;

    private TournamentState state;

    private List<PlayerDTO> sortedBracketsPlayers;

    @Override
    public String toString() {
        return "TournamentDTO{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", players=" + players +
                ", state=" + state +
                '}';
    }
}
