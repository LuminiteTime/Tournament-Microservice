package university.innopolis.tabletennis.tournamentmicroservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoundDTO {
    @NotNull(message = "Matches are not provided")
    private List<MatchDTO> matches;
}
