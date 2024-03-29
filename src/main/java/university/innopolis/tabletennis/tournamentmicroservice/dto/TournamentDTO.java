package university.innopolis.tabletennis.tournamentmicroservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.builderinterface.ITournamentDTOBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class TournamentDTO {
    private String title;
    private LocalDate date;
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
