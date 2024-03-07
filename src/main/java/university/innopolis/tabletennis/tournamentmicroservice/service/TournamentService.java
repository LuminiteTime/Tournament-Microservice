package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.PlayersListRequest;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.TournamentRequest;

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
        return tournamentRepository.findAll();
    }

    public List<Match> retrieveMatches() {
        return matchRepository.findAll();
    }

    public List<GameTable> retrieveGameTables() {
        return gameTableRepository.findAll();
    }

    public List<Round> retrieveRound() {
        return roundRepository.findAll();
    }

    public Tournament addTournament(TournamentRequest tournamentRequest) {
        Tournament tournament = this.createTournament(tournamentRequest.getPlayersList());
        playerRepository.saveAll(tournamentRequest.getPlayersList());
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

    public List<Player> addPlayers(PlayersListRequest playersList) {
        playerRepository.saveAll(playersList.getPlayersList());
        return playersList.getPlayersList();
    }

    public List<Match> retrieveAvailableMatches() {
        return matchRepository.findAvailableMatches();
    }

//    public Player patchPlayingStatus(Long id) {
//        Optional<Player> player = playerRepository.findById(id);
//        if (player.isPresent()) {
//            player.get().setIsPlaying(true);
//            playerRepository.save(player.get());
//            return player.get();
//        }
//        throw new IllegalArgumentException("Player with id " + id + " does not exist.");
//    }

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

    public Match setMatchIsCompleted(Long id) {
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

            matchRepository.save(match.get());
            return match.get();
        } else {
            throw new IllegalArgumentException("Match with id " + id + " haven't started yet.");
        }
    }
}
