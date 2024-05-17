package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.BracketsMatch;

public interface BracketsMatchRepository extends JpaRepository<BracketsMatch, Long> {
}
