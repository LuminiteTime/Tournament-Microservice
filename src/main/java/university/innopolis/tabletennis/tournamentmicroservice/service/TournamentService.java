package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PostPlayersListRequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public List<Player> retrievePlayers() {
        return playerRepository.findAll();
    }

    public List<Tournament> retrieveTournaments() {
        return tournamentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Match> retrieveMatches(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");
        return tournament.get()
                .getMatchesOfTournament();
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
        return tournament.get()
                .getRoundsOfTournament();
    }

    // TODO: Decide what to do with RequestBody
    public Tournament addTournament() {
        List<Player> playersToAdd = playerRepository.findAll();
        Tournament tournament = this.createTournament(playersToAdd);
        matchRepository.saveAll(tournament.getMatchesOfTournament());
        roundRepository.saveAll(tournament.getRoundsOfTournament());
        gameTableRepository.saveAll(tournament.getTablesOfTournament());
        tournamentRepository.save(tournament);
        return tournament;
    }

    public Player addPlayer(Player playerToAdd) {
        for (Player player: playerRepository.findAll()) {
            if (player.getName().equals(playerToAdd.getName())) throw new IllegalArgumentException(
                    "Player with name " + playerToAdd.getName() + " already exists.");
        }
        playerRepository.save(playerToAdd);
        return playerToAdd;
    }

    public List<Player> addPlayers(PostPlayersListRequestBody playersList) {
        playerRepository.saveAll(playersList.getPlayersList());
        return playersList.getPlayersList();
    }

    public List<Match> retrieveAvailableMatches(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");

        List<Match> allMatches = tournament.get()
                .getMatchesOfTournament()
                .stream()
                .sorted(Comparator.comparingInt(Match::getGameTableIndex))
                .sorted(Comparator.comparingInt(Match::getRoundIndex))
                .toList();
        List<Match> availableMatches = new ArrayList<>();
        for (Match match: allMatches) {
            if (match.getFirstPlayer().getIsPlaying() || match.getSecondPlayer().getIsPlaying()
                    || match.getIsCompleted() || match.getIsBeingPlayed()) {
                continue;
            }
            availableMatches.add(match);
        }
        return availableMatches;
    }

    public Match setMatchIsBeingPlayed(Long tournamentId, Long matchId) {
        // TODO: Do we need to find match in the specific tournament or do we operate with only one tournament?
        Optional<Match> match = matchRepository.findById(matchId);
        if (match.isEmpty()) throw new IllegalArgumentException("Match with id " + matchId + " does not exist.");
        if (match.get().getIsBeingPlayed()) throw new IllegalArgumentException("Match with id " + matchId + " is already being played.");
        if (match.get().getIsCompleted()) throw new IllegalArgumentException("Match with id " + matchId + " is completed.");

        match.get().setIsBeingPlayed(true);

        Player firstPlayer = match.get().getFirstPlayer();
        Player secondPlayer = match.get().getSecondPlayer();
        firstPlayer.setIsPlaying(true);
        secondPlayer.setIsPlaying(true);
        playerRepository.save(firstPlayer);
        playerRepository.save(secondPlayer);

        matchRepository.save(match.get());
        return match.get();
    }

    public Match setMatchIsCompleted(Long tournamentId, Long matchId, PatchMatchRequestBody matchToPatch) {
        // TODO: Do we need to find match in the specific tournament or do we operate with only one tournament?
        Optional<Match> match = matchRepository.findById(matchId);
        if (match.isEmpty()) throw new IllegalArgumentException("Match with id " + matchId + " does not exist.");
        if (match.get().getIsCompleted()) throw new IllegalArgumentException("Match with id " + matchId + " is completed.");
        if (!match.get().getIsBeingPlayed()) throw new IllegalArgumentException("Match with id " + matchId + " haven't started yet.");

        match.get().setIsCompleted(true);
        match.get().setIsBeingPlayed(false);

        Player firstPlayer = match.get().getFirstPlayer();
        Player secondPlayer = match.get().getSecondPlayer();
        firstPlayer.setIsPlaying(false);
        secondPlayer.setIsPlaying(false);
        playerRepository.save(firstPlayer);
        playerRepository.save(secondPlayer);

        match.get().setFirstPlayerScore(matchToPatch.getFirstPlayerScore());
        match.get().setSecondPlayerScore(matchToPatch.getSecondPlayerScore());

        matchRepository.save(match.get());
        return match.get();
    }

    public Tournament retrieveTournament(Long tournamentId) {
        Optional<Tournament> tournament = tournamentRepository.findById(tournamentId);
        if (tournament.isEmpty()) throw new IllegalArgumentException("Tournament with id " + tournamentId + " does not exist.");
        return tournament.get();
    }

    public Player deletePlayer(Long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isEmpty()) throw new IllegalArgumentException("Player with id " + playerId + " does not exist.");
        playerRepository.deleteById(playerId);
        return player.get();
    }
}
