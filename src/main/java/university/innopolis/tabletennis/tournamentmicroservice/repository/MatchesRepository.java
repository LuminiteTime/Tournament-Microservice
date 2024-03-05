package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;

import java.util.Optional;

public interface MatchesRepository extends JpaRepository<Match, Long> {
//    @Override
//    Optional<Match> findById(Long id);
}
