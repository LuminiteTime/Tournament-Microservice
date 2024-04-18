package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private int code;
    private String message;
    private Long timestamp;
}
