package university.innopolis.tabletennis.tournamentmicroservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.builderinterface.IMatchDTOBuilder;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Getter
@NoArgsConstructor
public class MatchDTO {
    private Long firstPlayerId;
    private Long secondPlayerId;
    private Integer firstPlayerScore;
    private Integer secondPlayerScore;
    private MatchState state;

    private MatchDTO(MatchDTOBuilder builder) {
        this.firstPlayerId = builder.firstPlayerId;
        this.secondPlayerId = builder.secondPlayerId;
        this.firstPlayerScore = builder.firstPlayerScore;
        this.secondPlayerScore = builder.secondPlayerScore;
        this.state = builder.state;
    }

    @NoArgsConstructor
    public static class MatchDTOBuilder implements IMatchDTOBuilder {
        private Long firstPlayerId;
        private Long secondPlayerId;
        private Integer firstPlayerScore;
        private Integer secondPlayerScore;
        private MatchState state;

        @Override
        public MatchDTOBuilder firstPlayerId(Long firstPlayerId) {
            this.firstPlayerId = firstPlayerId;
            return this;
        }

        @Override
        public MatchDTOBuilder secondPlayerId(Long secondPlayerId) {
            this.secondPlayerId = secondPlayerId;
            return this;
        }

        @Override
        public MatchDTOBuilder firstPlayerScore(Integer firstPlayerScore) {
            this.firstPlayerScore = firstPlayerScore;
            return this;
        }

        @Override
        public MatchDTOBuilder secondPlayerScore(Integer secondPlayerScore) {
            this.secondPlayerScore = secondPlayerScore;
            return this;
        }

        @Override
        public MatchDTOBuilder state(MatchState state) {
            this.state = state;
            return this;
        }

        @Override
        public MatchDTO build() {
            return new MatchDTO(this);
        }
    }
}
