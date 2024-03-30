package university.innopolis.tabletennis.tournamentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.builderinterface.ITournamentDTOBuilder;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "Tournament", description = "Tournament DTO")
@Getter
@NoArgsConstructor
public class TournamentDTO {

    @Schema(description = "Title of the tournament, must be unique", example = "Beginners 15")
    @NotBlank(message = "Title is not provided")
    private String title;

    @Schema(description = "Date of the tournament")
    @NotNull(message = "Date is not provided")
    private LocalDate date;

    @Schema(description = "Players of the tournament")
    @NotNull(message = "Players are not provided")
    private List<PlayerDTO> players;

    @Schema(description = "State of the tournament", example = "PLAYING")
    @NotNull(message = "State is not provided")
    private TournamentState state;

    private TournamentDTO(TournamentDTOBuilder builder) {
        this.title = builder.title;
        this.date = builder.date;
        this.players = builder.players;
        this.state = builder.state;
    }

    @NoArgsConstructor
    public static class TournamentDTOBuilder implements ITournamentDTOBuilder {
        private String title;
        private LocalDate date;
        private List<PlayerDTO> players;
        private TournamentState state;

        public TournamentDTOBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TournamentDTOBuilder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public TournamentDTOBuilder players(List<PlayerDTO> players) {
            this.players = players;
            return this;
        }

        public TournamentDTOBuilder state(TournamentState state) {
            this.state = state;
            return this;
        }

        public TournamentDTO build() {
            return new TournamentDTO(this);
        }
    }
}
