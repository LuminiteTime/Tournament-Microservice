package university.innopolis.tabletennis.tournamentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import university.innopolis.tabletennis.tournamentmicroservice.builderinterface.IMatchDTOBuilder;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

@Schema(name = "Match", description = "Match DTO")
@Getter
@NoArgsConstructor
public class MatchDTO {
    @Schema(description = "Id of the first player", example = "34")
    @NotNull(message = "Id is not provided")
    @Min(value = 1, message = "Invalid id format")
    private Long firstPlayerId;

    @Schema(description = "Id of the second player", example = "56")
    @NotNull(message = "Id is not provided")
    @Min(value = 1, message = "Invalid id format")
    private Long secondPlayerId;

    @Schema(description = "Score of the first player", example = "2")
    @NotNull(message = "Score for the first player is not provided")
    @Min(value = 0, message = "Invalid score")
    private Integer firstPlayerScore;

    @Schema(description = "Score of the second player", example = "1")
    @NotNull(message = "Score for the second player is not provided")
    @Min(value = 0, message = "Invalid score")
    private Integer secondPlayerScore;

    @Schema(description = "State of the match", example = "PLAYING")
    @NotNull(message = "State of the match is not provided")
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
