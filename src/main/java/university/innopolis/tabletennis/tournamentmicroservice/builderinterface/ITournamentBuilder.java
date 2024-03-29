package university.innopolis.tabletennis.tournamentmicroservice.builderinterface;

import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Tournament;

import java.time.LocalDate;
import java.util.List;

public interface ITournamentBuilder {
    Tournament.TournamentBuilder title(String title);

    Tournament.TournamentBuilder date(LocalDate date);

    Tournament.TournamentBuilder players(List<Player> players);

    Tournament build();
}
