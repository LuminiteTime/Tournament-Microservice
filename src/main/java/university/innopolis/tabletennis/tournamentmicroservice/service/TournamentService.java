package university.innopolis.tabletennis.tournamentmicroservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.TournamentDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;
import university.innopolis.tabletennis.tournamentmicroservice.exception.TournamentNotFoundException;
import university.innopolis.tabletennis.tournamentmicroservice.repository.*;
import university.innopolis.tabletennis.tournamentmicroservice.states.TournamentState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TournamentService {

    private final PlayerRepository playerRepository;

    private final TablesMatchRepository tablesMatchRepository;

    private final GameTableRepository gameTableRepository;

    private final TournamentRepository tournamentRepository;

    public List<Tournament> retrieveAllTournaments() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        for (Tournament tournament : tournaments) {
            if (tournament.getState().equals(TournamentState.FINISHED))
                tournament.setSortedBracketsPlayers(computeSortedPlayersForBrackets(tournament));
        }
        return tournaments;
    }

    public Tournament retrieveTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->
                new TournamentNotFoundException(tournamentId));
        if (tournament.getState().equals(TournamentState.FINISHED))
            tournament.setSortedBracketsPlayers(computeSortedPlayersForBrackets(tournament));
        return tournament;
    }

    public List<GameTable> retrieveGameTables(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new TournamentNotFoundException(tournamentId)
                );
        return tournament.getTablesOfTournament();
    }

    public Tournament addTournament(TournamentDTO tournamentDTO) {
        List<Player> playersToAdd = tournamentDTO.getPlayers().stream()
                .map(MappingUtils::mapToPlayerEntity)
                .toList();
        if (tournamentRepository.findAll().stream()
                .map(Tournament::getTitle)
                .toList()
                .contains(tournamentDTO.getTitle())) {
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

        for (GameTable table : tablesOfTournament) {
            tablesMatchRepository.saveAll(table.getTablesMatches());
        }

        gameTableRepository.saveAll(tournament.getTablesOfTournament());

        tournamentRepository.save(tournament);
        log.warn("Tournament with id {} is saved.", tournament.getId());
        return tournament;
    }

    public Tournament finishTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new TournamentNotFoundException(tournamentId)
                );
        tournament.setState(TournamentState.FINISHED);
        tournament.setSortedBracketsPlayers(computeSortedPlayersForBrackets(tournament));
        tournamentRepository.save(tournament);
        return tournament;
    }

    public List<TablesMatch> retrieveAllMatches(Long tournamentId) {
        Tournament tournament = tournamentRepository
                .findById(tournamentId)
                .orElseThrow(() ->
                        new TournamentNotFoundException(tournamentId)
                );
        List<TablesMatch> allTablesMatches = new ArrayList<>();
        for (GameTable table : tournament.getTablesOfTournament()) {
            allTablesMatches.addAll(table.getTablesMatches());
        }
        return allTablesMatches.stream()
                .sorted(Comparator.comparing(TablesMatch::getId))
                .toList();
    }

    // TODO: Add comparing by score if players have the same amount of points.
    private List<Player> computeSortedPlayersForBrackets(Tournament tournament) {
        log.info("Computing sorted list of players for brackets.");

        List<List<Player>> sortedPlayersByGameTables = new ArrayList<>();
        for (GameTable gameTable : tournament.getTablesOfTournament()) {
            log.info("Sorting players of game table with id: {}", gameTable.getId());
            sortedPlayersByGameTables.add(sortPlayersOfGameTableByPlayedGames(gameTable));
        }

        List<Player> sortedPlayers = new ArrayList<>();
        int gameTableIndex = 0;
        int playerIndex = 0;
        for (int i = 0; i < tournament.getPlayers().size(); i++) {
            List<Player> sortedGameTablePlayers = sortedPlayersByGameTables.get(gameTableIndex);
            sortedPlayers.add(sortedGameTablePlayers.get(playerIndex));
            if (gameTableIndex + 1 >= tournament.getTablesOfTournament().size()) {
                gameTableIndex = 0;
                playerIndex++;
            } else {
                gameTableIndex++;
            }
        }
        return  sortedPlayers;
    }

    private static TablesMatch findMatchByTwoPlayers(GameTable gameTable, Player player1, Player player2) {
        return gameTable.getTablesMatches().stream()
                .filter(match -> (match.getFirstPlayer().equals(player1) && match.getSecondPlayer().equals(player2)) ||
                        (match.getFirstPlayer().equals(player2) && match.getSecondPlayer().equals(player1)))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Match with players " + player1 + " and " + player2 + " not found.")
                );
    }

    private static List<Player> sortPlayersOfGameTableByPlayedGames(GameTable gameTable) {
        Map<Player, Integer> playerGamesScoreMap = getPlayerGamesScoreMap(gameTable);
        for (Map.Entry<Player, Integer> entry : playerGamesScoreMap.entrySet()) {
            log.info("Player with id {} has score {}", entry.getKey().getId(), entry.getValue());
        }
        log.info("===========================");

        Map<Integer, List<Player>> scorePlayersMap = playerGamesScoreMap.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
        scorePlayersMap.forEach((score, players) -> log.info("Players with score {}: {}", score, players.stream().mapToLong(Player::getId).toArray()));

        TreeMap<Integer, List<Player>> sortedScorePlayersMap = new TreeMap<>(Comparator.reverseOrder());
        sortedScorePlayersMap.putAll(scorePlayersMap);

        if (scorePlayersMap.keySet().size() == gameTable.getPlayers().size()) {
            return sortedScorePlayersMap.values().stream()
                    .flatMap(List::stream)
                    .toList();
        }

        List<Player> sortedPlayers = new ArrayList<>();
        for (Map.Entry<Integer, List<Player>> entry : sortedScorePlayersMap.entrySet()) {
            List<Player> playersWithTheScore = entry.getValue();
            if (playersWithTheScore.size() == 1) {
                sortedPlayers.add(playersWithTheScore.get(0));
            } else {
                Map<Player, Double> playersRatio = new HashMap<>();
                for (int i = 0; i < playersWithTheScore.size(); i++) {
                    Player player = playersWithTheScore.get(i);
                    int wins = 0;
                    int loses = 0;
                    for (int j = 0; j < playersWithTheScore.size(); j++) {
                        if (i != j) {
                            TablesMatch match = findMatchByTwoPlayers(gameTable, player, playersWithTheScore.get(j));
                            if (match.getFirstPlayer().equals(player)) {
                                if (match.getFirstPlayerScore() > match.getSecondPlayerScore()) {
                                    wins++;
                                } else {
                                    loses++;
                                }
                            } else {
                                if (match.getFirstPlayerScore() < match.getSecondPlayerScore()) {
                                    wins++;
                                } else {
                                    loses++;
                                }
                            }
                        }
                    }
                    playersRatio.put(player, (double) wins / loses);
                }
                List<Player> sortedPlayersWithTheScore = playersRatio.entrySet().stream()
                        .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                        .map(Map.Entry::getKey)
                        .toList();
                sortedPlayers.addAll(sortedPlayersWithTheScore);
            }
        }

        return sortedPlayers;
    }

    private static Map<Player, Integer> getPlayerGamesScoreMap(GameTable gameTable) {
        Map<Player, Integer> playerGamesScoresMap = new HashMap<>();
        for (TablesMatch match : gameTable.getTablesMatches()) {
            Player firstPlayer = match.getFirstPlayer();
            Player secondPlayer = match.getSecondPlayer();
            playerGamesScoresMap.put(firstPlayer,
                    playerGamesScoresMap.getOrDefault(firstPlayer, 0)
                            + (match.getFirstPlayerScore() > match.getSecondPlayerScore() ? 2 : 1));
            playerGamesScoresMap.put(secondPlayer,
                    playerGamesScoresMap.getOrDefault(secondPlayer, 0)
                            + (match.getFirstPlayerScore() < match.getSecondPlayerScore() ? 2 : 1));
        }
        return playerGamesScoresMap;
    }
}
