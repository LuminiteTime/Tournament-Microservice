package university.innopolis.tabletennis.tournamentmicroservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.GameTable;
import university.innopolis.tabletennis.tournamentmicroservice.entity.TablesMatch;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
import university.innopolis.tabletennis.tournamentmicroservice.exception.GameTableNotFoundException;
import university.innopolis.tabletennis.tournamentmicroservice.repository.GameTableRepository;
import university.innopolis.tabletennis.tournamentmicroservice.repository.TablesMatchRepository;
import university.innopolis.tabletennis.tournamentmicroservice.dto.PatchMatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.states.MatchState;
import university.innopolis.tabletennis.tournamentmicroservice.states.PlayerState;
import university.innopolis.tabletennis.tournamentmicroservice.utils.MappingUtils;
import university.innopolis.tabletennis.tournamentmicroservice.utils.validation.MatchInfoValidationResult;
import university.innopolis.tabletennis.tournamentmicroservice.utils.validation.ValidationUtils;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class MatchService {

    private final TablesMatchRepository tablesMatchRepository;

    private final GameTableRepository gameTableRepository;

    public MatchDTO patchMatchState(Long matchId, Optional<PatchMatchDTO> matchInfo) {
        TablesMatch tablesMatch = tablesMatchRepository.findById(matchId)
                .orElseThrow(() -> {
                            log.warn("Match with id {} does not exist", matchId);
                            return new IllegalArgumentException("Match with id " + matchId + " does not exist.");
                        }
                );

        MatchInfoValidationResult validationResult = ValidationUtils.validateMatchInfo(tablesMatch, matchInfo, matchId);

        switch (validationResult) {
            case ALREADY_COMPLETED -> MappingUtils.mapToMatchDTO(tablesMatch);
            case READY_TO_START -> setMatchIsBeingPlayed(tablesMatch);
            case READY_TO_COMPLETE -> setMatchIsCompleted(tablesMatch, matchInfo.get());
            default -> {
                log.error("Unknown error occurred while patching the match");
                throw new IllegalArgumentException("Unknown error occurred while patching the match.");
            }
        }

        tablesMatchRepository.save(tablesMatch);

        return MappingUtils.mapToMatchDTO(tablesMatch);
    }

    private void setMatchIsCompleted(TablesMatch tablesMatch, PatchMatchDTO matchInfo) {
        tablesMatch.setState(MatchState.COMPLETED);
        tablesMatch.setFirstPlayerScore(matchInfo.getFirstPlayerScore());
        tablesMatch.setSecondPlayerScore(matchInfo.getSecondPlayerScore());
        log.info("Match with id {} is completed", tablesMatch.getId());
    }

    private void setMatchIsBeingPlayed(TablesMatch tablesMatch) {
        tablesMatch.setState(MatchState.PLAYING);
        log.info("Match with id {} has been started", tablesMatch.getId());
    }

    @Transactional
    public List<MatchDTO> retrieveAvailableMatches(Long tableId) {
        GameTable gameTable = gameTableRepository
                .findById(tableId)
                .orElseThrow(() -> {
                            log.warn("Game table with id {} does not exist", tableId);
                            return new GameTableNotFoundException(tableId);
                        }
                );

        List<TablesMatch> availableTablesMatches = new ArrayList<>();
        Map<Player, PlayerState> tempBusy = new HashMap<>();

        for (Player player: gameTable.getPlayers()) {
            tempBusy.put(player, PlayerState.FREE);
        }

        for (TablesMatch tablesMatch: gameTable.getTablesMatches()) {
            if (Boolean.TRUE.equals(!tablesMatch.getState().equals(MatchState.NOT_PLAYING) ||
                    tempBusy.get(tablesMatch.getFirstPlayer()).isBusy()) ||
                    Boolean.TRUE.equals(tempBusy.get(tablesMatch.getSecondPlayer()).isBusy()))
                continue;

            availableTablesMatches.add(tablesMatch);
            tempBusy.put(tablesMatch.getFirstPlayer(), PlayerState.PLAYING);
            tempBusy.put(tablesMatch.getSecondPlayer(), PlayerState.PLAYING);
        }
        return availableTablesMatches.stream()
                .map(MappingUtils::mapToMatchDTO)
                .toList();
    }
}
