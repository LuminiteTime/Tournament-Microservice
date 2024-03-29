package university.innopolis.tabletennis.tournamentmicroservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoundDTO {
    private List<MatchDTO> matches;
}
