package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PatchMatchRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PostPlayersListRequestBody;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PostTournamentRequestBody;

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
        return playerRepository.findAll(Sort.by(Sort.Direction.DESC, "rating"));
    }

    public List<Tournament> retrieveTournaments() {
        return tournamentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Match> retrieveMatches() {
        return matchRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<GameTable> retrieveGameTables() {
        return gameTableRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public List<Round> retrieveRounds() {
        return roundRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    // TODO: Decide what to do with RequestBody
    public Tournament addTournament(PostTournamentRequestBody postTournamentRequestBody) {
//        List<Player> playersToAdd = postTournamentRequestBody.getPlayersList();
        List<Player> playersToAdd = playerRepository.findAll();
        Tournament tournament = this.createTournament(playersToAdd);
        playerRepository.saveAll(postTournamentRequestBody.getPlayersList());
        matchRepository.saveAll(tournament.getMatchesOfTournament());
        roundRepository.saveAll(tournament.getRoundsOfTournament());
        gameTableRepository.saveAll(tournament.getTablesOfTournament());
        tournamentRepository.save(tournament);
        return tournament;
    }

    public Player addPlayer(Player playerToAdd) {
        List<Player> playersNowInTournament = playerRepository.findAll();
        for (Player player: playersNowInTournament) {
            if (player.equals(playerToAdd)) return playerToAdd;
        }
        playerRepository.save(playerToAdd);
        return playerToAdd;
    }

    public List<Player> addPlayers(PostPlayersListRequestBody playersList) {
        playerRepository.saveAll(playersList.getPlayersList());
        return playersList.getPlayersList();
    }

    public List<Match> retrieveAvailableMatches() {
        return matchRepository.findAvailableMatches();
    }

    public Match setMatchIsBeingPlayed(Long id) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isEmpty()) throw new IllegalArgumentException("Match with id " + id + " does not exist.");
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

    public Match setMatchIsCompleted(Long id, PatchMatchRequestBody matchToPatch) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isEmpty()) throw new IllegalArgumentException("Match with id " + id + " does not exist.");
        if (match.get().getIsBeingPlayed()) {
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
        } else {
            throw new IllegalArgumentException("Match with id " + id + " haven't started yet.");
        }
    }
}
