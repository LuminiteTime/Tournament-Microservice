package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.LoserBracketsMatch;

public interface LoserBracketsMatchRepository extends JpaRepository<LoserBracketsMatch, Long> {
}
