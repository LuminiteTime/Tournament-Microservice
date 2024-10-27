package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO playerDTO = (PlayerDTO) o;
        return Objects.equals(getExternalId(), playerDTO.getExternalId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getExternalId());
    }
}
