package university.innopolis.tabletennis.tournamentmicroservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "Player", description = "Player DTO")
@Getter
@Setter
@NoArgsConstructor
public class PlayerDTO {
    @Schema(description = "Id of a player in the external database", example = "34")
    @NotNull(message = "Id is not provided")
    @Min(value = 1, message = "Invalid id format")
    private Long externalId;
}
