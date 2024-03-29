package university.innopolis.tabletennis.tournamentmicroservice.builderinterface;

import university.innopolis.tabletennis.tournamentmicroservice.dto.PlayerDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;

import java.time.LocalDate;
import java.util.List;

public interface ITournamentDTOBuilder {
    TournamentDTO.TournamentDTOBuilder title(String title);

    TournamentDTO.TournamentDTOBuilder date(LocalDate date);

    TournamentDTO.TournamentDTOBuilder players(List<PlayerDTO> players);

    TournamentDTO build();
}
