package university.innopolis.tabletennis.tournamentmicroservice.utils;

import org.springframework.stereotype.Service;
import university.innopolis.tabletennis.tournamentmicroservice.dto.*;
import university.innopolis.tabletennis.tournamentmicroservice.entity.*;

@Service
public class MappingUtils {

    static public TournamentDTO mapToTournamentDTO(Tournament entity) {
        return new TournamentDTO.TournamentDTOBuilder()
                .title(entity.getTitle())
                .date(entity.getDate())
                .players(entity.getPlayers().stream()
                        .map(MappingUtils::mapToPlayerDTO)
                        .toList())
                .build();
    }

    public static GameTableDTO mapToGameTableDTO(GameTable entity) {
        return new GameTableDTO.GameTableDTOBuilder()
                .players(entity.getPlayers().stream()
                        .map(MappingUtils::mapToPlayerDTO)
                        .toList())
                .rounds(entity.getRounds().stream()
                        .map(MappingUtils::mapToRoundDTO)
                        .toList())
                .matches(entity.getMatches().stream()
                        .map(MappingUtils::mapToMatchDTO)
                        .toList())
                .build();
    }

    public static MatchDTO mapToMatchDTO(Match entity) {
        return new MatchDTO.MatchDTOBuilder()
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

    public static RoundDTO mapToRoundDTO(Round entity) {
        RoundDTO dto = new RoundDTO();
        dto.setMatches(entity.getMatches().stream()
                .map(MappingUtils::mapToMatchDTO)
                .toList());
        return dto;
    }
}
