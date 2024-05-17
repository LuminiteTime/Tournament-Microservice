package university.innopolis.tabletennis.tournamentmicroservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.*;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;

import java.time.LocalDate;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MappingUtils {

    public static TournamentDTO mapToTournamentDTO(Tournament entity) {
        return TournamentDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .date(LocalDate.now())
                .players(entity.getPlayers().stream()
                        .map(MappingUtils::mapToPlayerDTO)
                        .toList())
                .amountOfTables(entity.getTablesOfTournament().size())
                .state(entity.getState())
                .build();
    }

    public static GameTableDTO mapToGameTableDTO(GameTable entity) {
        return GameTableDTO.builder()
                .id(entity.getId())
                .players(entity.getPlayers().stream()
                        .map(MappingUtils::mapToPlayerDTO)
                        .toList())
                .matches(entity.getTablesMatches().stream()
                        .map(MappingUtils::mapToMatchDTO)
                        .toList())
                .build();
    }

    public static MatchDTO mapToMatchDTO(TablesMatch entity) {
        return MatchDTO.builder()
                .id(entity.getId())
                .firstPlayerId(entity.getFirstPlayer().getExternalId())
                .secondPlayerId(entity.getSecondPlayer().getExternalId())
                .firstPlayerScore(entity.getFirstPlayerScore())
                .secondPlayerScore(entity.getSecondPlayerScore())
                .state(entity.getState())
                .build();
    }

    public static PlayerDTO mapToPlayerDTO(Player entity) {
        PlayerDTO dto = new PlayerDTO();
        dto.setExternalId(entity.getExternalId());
        return dto;
    }

    public static Player mapToPlayerEntity(PlayerDTO dto) {
        Player entity = new Player();
        entity.setExternalId(dto.getExternalId());
        return entity;
    }
}
