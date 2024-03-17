package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.IdListRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PostPlayersListRequestBody;

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

    public Tournament createTournament(List<Player> players) {
        return new Tournament(players);
    }

    public List<Player> retrievePlayers(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");
        return tournament.get().getPlayersOfTournament();
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

    public List<Round> retrieveRounds(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");
        List<Round> rounds = new ArrayList<>();
        for (GameTable table: tournament.get().getTablesOfTournament()) {
            rounds.addAll(table.getRoundsOfTable());
        }
        return rounds;
    }

    // TODO: Decide what to do with RequestBody
    public Tournament addTournament() {
        List<Player> playersToAdd = playerRepository.findAll();
        Tournament tournament = this.createTournament(playersToAdd);
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

    public List<Player> addPlayers(Long tournamentId, PostPlayersListRequestBody playersList) {
        playerRepository.saveAll(playersList.getPlayersList());
        return playersList.getPlayersList();
    }

    public List<Match> retrieveAvailableMatches(Long tournamentId) {
        List<Match> allMatches = retrieveMatches(tournamentId);
        List<Match> availableMatches = new ArrayList<>();
        Map<Player, Boolean> tempBusy = new HashMap<>();

        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) return availableMatches;
        for (Player player: tournament.get().getPlayersOfTournament()) {
            tempBusy.put(player, false);
        }

        for (Match match: allMatches) {
            if (match.getFirstPlayer().getIsPlaying() || match.getSecondPlayer().getIsPlaying()
                    || match.getIsCompleted() || match.getIsBeingPlayed() || tempBusy.get(match.getFirstPlayer())
                    || tempBusy.get(match.getSecondPlayer())) {
                continue;
            }
            availableMatches.add(match);
            tempBusy.put(match.getFirstPlayer(), true);
            tempBusy.put(match.getSecondPlayer(), true);
        }
        return availableMatches;
    }

    public Tournament retrieveTournament(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");
        return tournament.get();
    }

    public Player deletePlayer(Long tournamentId, Long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isEmpty()) throw new IllegalArgumentException("Player with id " + playerId + " does not exist.");
        playerRepository.deleteById(playerId);
        return player.get();
    }

}
