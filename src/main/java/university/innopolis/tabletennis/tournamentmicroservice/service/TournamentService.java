package university.innopolis.tabletennis.tournamentmicroservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.exception.TournamentNotFoundException;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class TournamentService {

    private final PlayerRepository playerRepository;

    private final TablesMatchRepository tablesMatchRepository;

    private final GameTableRepository gameTableRepository;

    private final TournamentRepository tournamentRepository;

    public List<Tournament> retrieveAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament retrieveTournament(Long tournamentId) {
        return tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new TournamentNotFoundException(tournamentId)
        );
    }

    public List<GameTable> retrieveGameTables(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new TournamentNotFoundException(tournamentId)
                );
        return tournament.getTablesOfTournament();
    }

    public Tournament addTournament(TournamentDTO tournamentDTO, List<Player> playersToAdd) {
        if (tournamentRepository.findAll().stream()
                .map(Tournament::getTitle)
                .toList()
                .contains(tournamentDTO.getTitle()))
        {
            log.warn("Title must be unique.");
            throw new IllegalArgumentException("Title must be unique.");
        }

        playerRepository.saveAll(playersToAdd);

        Tournament tournament = new Tournament.TournamentBuilder()
                .title(tournamentDTO.getTitle())
                .players(playersToAdd)
                .desiredNumberOfTables(tournamentDTO.getAmountOfTables())
                .build();
        List<GameTable> tablesOfTournament = tournament.getTablesOfTournament();

        for (GameTable table: tablesOfTournament) {
            tablesMatchRepository.saveAll(table.getTablesMatches());
        }

        gameTableRepository.saveAll(tournament.getTablesOfTournament());

        tournamentRepository.save(tournament);
        log.debug("Tournament is saved.");
        return tournament;
    }

    public Tournament patchTournamentState(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new TournamentNotFoundException(tournamentId)
                );
        tournament.setState(TournamentState.FINISHED);
        tournamentRepository.save(tournament);
        return tournament;
    }

    public List<TablesMatch> retrieveALlMatches(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new TournamentNotFoundException(tournamentId)
                );
        List<TablesMatch> allTablesMatches = new ArrayList<>();
        for (GameTable table: tournament.getTablesOfTournament()) {
            allTablesMatches.addAll(table.getTablesMatches());
        }
        return allTablesMatches.stream()
                .sorted(Comparator.comparing(TablesMatch::getId))
                .toList();
    }
}
