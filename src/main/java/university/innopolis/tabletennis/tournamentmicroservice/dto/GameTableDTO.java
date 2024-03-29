package university.innopolis.tabletennis.tournamentmicroservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.builderinterface.IGameTableDTOBuilder;

import java.util.List;

@Getter
public class GameTableDTO {
    private final List<PlayerDTO> players;
    private final List<RoundDTO> rounds;
    private final List<MatchDTO> matches;

    private GameTableDTO(GameTableDTOBuilder builder) {
        this.players = builder.players;
        this.rounds = builder.rounds;
        this.matches = builder.matches;
    }

    @NoArgsConstructor
    public static class GameTableDTOBuilder implements IGameTableDTOBuilder {

        private List<PlayerDTO> players;
        private List<RoundDTO> rounds;
        private List<MatchDTO> matches;

        @Override
        public GameTableDTOBuilder players(List<PlayerDTO> players) {
            this.players = players;
            return this;
        }

        @Override
        public GameTableDTOBuilder rounds(List<RoundDTO> rounds) {
            this.rounds = rounds;
            return this;
        }

        @Override
        public GameTableDTOBuilder matches(List<MatchDTO> matches) {
            this.matches = matches;
            return this;
        }

        @Override
        public GameTableDTO build() {
            return new GameTableDTO(this);
        }
    }
}