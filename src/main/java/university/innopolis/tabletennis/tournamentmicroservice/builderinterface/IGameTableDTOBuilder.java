package university.innopolis.tabletennis.tournamentmicroservice.builderinterface;

import university.innopolis.tabletennis.tournamentmicroservice.dto.GameTableDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.RoundDTO;

import java.util.List;

public interface IGameTableDTOBuilder {
    GameTableDTO.GameTableDTOBuilder players(List<PlayerDTO> players);
    GameTableDTO.GameTableDTOBuilder rounds(List<RoundDTO> rounds);
    GameTableDTO.GameTableDTOBuilder matches(List<MatchDTO> matches);
    GameTableDTO build();
}
