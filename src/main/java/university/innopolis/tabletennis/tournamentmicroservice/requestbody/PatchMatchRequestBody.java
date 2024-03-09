package university.innopolis.tabletennis.tournamentmicroservice.requestbody;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchMatchRequestBody {
    private Integer firstPlayerScore;
    private Integer secondPlayerScore;
}
