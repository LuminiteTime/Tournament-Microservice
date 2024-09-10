package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.LoserBrackets;

public interface LoserBracketsRepository extends JpaRepository<LoserBrackets, Long> {
}
