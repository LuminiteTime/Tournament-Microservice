package university.innopolis.tabletennis.tournamentmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.exception.InvalidNumberOfPlayersException;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.requestbody.TournamentRequest;

import java.util.List;

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
}
