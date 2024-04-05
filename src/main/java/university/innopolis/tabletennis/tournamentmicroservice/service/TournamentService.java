package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import java.util.*;

@Service
public class TournamentService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private GameTableRepository gameTableRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    public List<Tournament> retrieveAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament retrieveTournament(Long id) {
        return tournamentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(
                        "Tournament with id " + id + " does not exist."
                )
        );
    }

    public List<GameTable> retrieveGameTables(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Tournament with id " + tournamentId + " does not exist."
                        )
                );
        return tournament.getTablesOfTournament();
    }

    public Tournament addTournament(TournamentDTO tournamentDTO, List<Player> playersToAdd) {
        if (tournamentRepository.findAll().stream()
                .map(Tournament::getTitle)
                .toList()
                .contains(tournamentDTO.getTitle()))
            throw new IllegalArgumentException("Title must be unique.");

        playerRepository.saveAll(playersToAdd);

        Tournament tournament = new Tournament.TournamentBuilder()
                .title(tournamentDTO.getTitle())
                .date(tournamentDTO.getDate())
                .players(playersToAdd)
                .build();
        List<GameTable> tablesOfTournament = tournament.getTablesOfTournament();

        // Saving matches.
        for (GameTable table: tablesOfTournament) {
            matchRepository.saveAll(table.getMatches());
        }

        // Saving rounds.
        for (GameTable table: tablesOfTournament) {
            roundRepository.saveAll(table.getRounds());
        }

        // Saving tables,
        gameTableRepository.saveAll(tournament.getTablesOfTournament());

        // Saving tournament.
        tournamentRepository.save(tournament);
        return tournament;
    }

    public Tournament patchTournamentState(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Tournament with id " + tournamentId + " does not exist."
                        )
                );
        tournament.setState(TournamentState.FINISHED);
        tournamentRepository.save(tournament);
        return tournament;
    }
}
