package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    @NotNull(message = "Id is not provided")
    @Min(value = 1, message = "Invalid id format")
    private Long externalId;

    @Override
    public String toString() {
        return "PlayerDTO{" +
                "externalId=" + externalId +
                '}';
    }
}
