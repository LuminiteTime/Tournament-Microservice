package university.innopolis.tabletennis.tournamentmicroservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private int code;
    private String message;
    private LocalDateTime timestamp;
}
