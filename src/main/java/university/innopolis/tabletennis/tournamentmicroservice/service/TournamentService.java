package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.GameTableDTO;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils;

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

    public List<TournamentDTO> retrieveAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(MappingUtils::mapToTournamentDTO)
                .toList();
    }

    public TournamentDTO retrieveTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(
                        "Tournament with id " + id + " does not exist."
                )
        );
        return MappingUtils.mapToTournamentDTO(tournament);
    }

    public List<GameTableDTO> retrieveGameTables(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Tournament with id " + tournamentId + " does not exist."
                        )
                );
        return tournament.getTablesOfTournament().stream()
                .map(MappingUtils::mapToGameTableDTO)
                .toList();
    }

    public TournamentDTO addTournament(TournamentDTO tournamentDTO) {
        if (tournamentRepository.findAll().stream()
                .map(Tournament::getTitle)
                .toList()
                .contains(tournamentDTO.getTitle()))
            throw new IllegalArgumentException("Title must be unique.");

        List<Player> playersToAdd = tournamentDTO.getPlayers().stream()
                .map(MappingUtils::mapToPlayerEntity)
                .toList();

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
        return MappingUtils.mapToTournamentDTO(tournament);
    }

    public TournamentDTO patchTournamentState(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Tournament with id " + tournamentId + " does not exist."
                        )
                );
        tournament.setState(TournamentState.FINISHED);
        tournamentRepository.save(tournament);
        return MappingUtils.mapToTournamentDTO(tournament);
    }
}
