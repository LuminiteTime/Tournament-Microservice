package university.innopolis.tabletennis.tournamentmicroservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.MatchDTO;
import university.innopolis.tabletennis.tournamentmicroservice.entity.GameTable;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;
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
        Match match = tablesMatchRepository.findById(matchId)
                .orElseThrow(() -> {
                            log.warn("Match with id {} does not exist", matchId);
                            return new IllegalArgumentException("Match with id " + matchId + " does not exist.");
                        }
                );

        MatchInfoValidationResult validationResult = ValidationUtils.validateMatchInfo(match, matchInfo, matchId);
        switch (validationResult) {
            case ALREADY_COMPLETED:
                return MappingUtils.mapToMatchDTO(match);
            case READY_TO_START:
                setMatchIsBeingPlayed(match);
                break;
            case READY_TO_COMPLETE:
                setMatchIsCompleted(match, matchInfo.get());
                break;
            default:
                log.error("Unknown error occurred while patching the match");
                throw new IllegalArgumentException("Unknown error occurred while patching the match.");
        }

        tablesMatchRepository.save(match);

        return MappingUtils.mapToMatchDTO(match);
    }

    private void setMatchIsCompleted(Match match, PatchMatchDTO matchInfo) {
        match.setState(MatchState.COMPLETED);
        match.setFirstPlayerScore(matchInfo.getFirstPlayerScore());
        match.setSecondPlayerScore(matchInfo.getSecondPlayerScore());
        log.info("Match with id {} is completed", match.getId());
    }

    private void setMatchIsBeingPlayed(Match match) {
        match.setState(MatchState.PLAYING);
        log.info("Match with id {} has been started", match.getId());
    }

    public List<MatchDTO> retrieveAvailableMatches(Long tableId) {
        GameTable gameTable = gameTableRepository
                .findById(tableId)
                .orElseThrow(() -> {
                            log.warn("Game table with id {} does not exist", tableId);
                            return new IllegalArgumentException("Game table with id " + tableId + " does not exist.");
                        }
                );

        List<Match> availableMatches = new ArrayList<>();
        Map<Player, PlayerState> tempBusy = new HashMap<>();

        for (Player player: gameTable.getPlayers()) {
            tempBusy.put(player, PlayerState.FREE);
        }

        for (Match match: gameTable.getMatches()) {
            if (Boolean.TRUE.equals(!match.getState().equals(MatchState.NOT_PLAYING) ||
                    tempBusy.get(match.getFirstPlayer()).isBusy()) ||
                    Boolean.TRUE.equals(tempBusy.get(match.getSecondPlayer()).isBusy()))
                continue;

            availableMatches.add(match);
            tempBusy.put(match.getFirstPlayer(), PlayerState.PLAYING);
            tempBusy.put(match.getSecondPlayer(), PlayerState.PLAYING);
        }
        return availableMatches.stream()
                .map(MappingUtils::mapToMatchDTO)
                .toList();
    }
}
