package university.innopolis.tabletennis.tournamentmicroservice.builderinterface;

import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;

public interface IMatchDTOBuilder {
    MatchDTO.MatchDTOBuilder firstPlayerId(Long firstPlayerId);
    MatchDTO.MatchDTOBuilder secondPlayerId(Long secondPlayerId);
    MatchDTO.MatchDTOBuilder firstPlayerScore(Integer firstPlayerScore);
    MatchDTO.MatchDTOBuilder secondPlayerScore(Integer secondPlayerScore);
    MatchDTO.MatchDTOBuilder state(MatchState state);
    MatchDTO build();
}
