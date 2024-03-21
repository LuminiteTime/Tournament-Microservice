package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.IdListRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.TournamentInfo;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MatchState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.PlayerState;

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

    public Tournament createTournament(TournamentInfo tournamentInfo) {
        return new Tournament(tournamentInfo);
    }

    public List<Tournament> retrieveTournaments(IdListRequestBody tournamentsList) {
        List<Tournament> tournaments = new ArrayList<>();
        if (tournamentsList.getIdList().isEmpty()) {
            return tournamentRepository.findAll();
        }
        for (Long id: tournamentsList.getIdList()) {
            Optional<Tournament> tournament = tournamentRepository.findById(id);
            if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + id + " does not exist.");
            tournaments.add(tournament.get());
        }
        return tournaments;
    }

    public List<Match> retrieveMatches(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");
        List<Match> matches = new ArrayList<>();
        for (GameTable table: tournament.get().getTablesOfTournament()) {
            matches.addAll(table.getMatchesOfTable());
        }
        return matches;
    }

    public List<GameTable> retrieveGameTables(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");
        return tournament.get()
                .getTablesOfTournament();
    }

    public Tournament addTournament(TournamentInfo tournamentInfo) {
        List<Player> playersToAdd = tournamentInfo.getPlayers();
        playerRepository.saveAll(playersToAdd);

        Tournament tournament = this.createTournament(tournamentInfo);
        List<GameTable> tablesOfTournament = tournament.getTablesOfTournament();

        // Saving matches.
        for (GameTable table: tablesOfTournament) {
            matchRepository.saveAll(table.getMatchesOfTable());
        }

        // Saving rounds.
        for (GameTable table: tablesOfTournament) {
            roundRepository.saveAll(table.getRoundsOfTable());
        }

        // Saving tables,
        gameTableRepository.saveAll(tournament.getTablesOfTournament());

        // Saving tournament.
        tournamentRepository.save(tournament);
        return tournament;
    }

    public List<Match> retrieveAvailableMatches(Long tournamentId) {
        List<Match> allMatches = retrieveMatches(tournamentId);
        List<Match> availableMatches = new ArrayList<>();
        Map<Player, PlayerState> tempBusy = new HashMap<>();

        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) return availableMatches;
        for (Player player: tournament.get().getPlayersOfTournament()) {
            tempBusy.put(player, PlayerState.FREE);
        }

        for (Match match: allMatches) {
            if (!match.getState().equals(MatchState.NOT_PLAYING) ||
                    tempBusy.get(match.getFirstPlayer()).isBusy() ||
                    tempBusy.get(match.getSecondPlayer()).isBusy())
                continue;

            availableMatches.add(match);
            tempBusy.put(match.getFirstPlayer(), PlayerState.PLAYING);
            tempBusy.put(match.getSecondPlayer(), PlayerState.PLAYING);
        }
        return availableMatches;
    }
}
