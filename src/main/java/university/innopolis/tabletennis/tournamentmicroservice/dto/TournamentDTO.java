package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.builderinterface.ITournamentDTOBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class TournamentDTO {

    @NotBlank(message = "Title is not provided.")
    private String title;

    @NotNull(message = "Date is not provided.")
    private LocalDate date;

    @NotNull(message = "Players are not provided.")
    private List<PlayerDTO> players;

    private TournamentDTO(TournamentDTOBuilder builder) {
        this.title = builder.title;
        this.date = builder.date;
        this.players = builder.players;
    }

    @NoArgsConstructor
    public static class TournamentDTOBuilder implements ITournamentDTOBuilder {
        private String title;
        private LocalDate date;
        private List<PlayerDTO> players;

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

        public TournamentDTO build() {
            return new TournamentDTO(this);
        }
    }
}
