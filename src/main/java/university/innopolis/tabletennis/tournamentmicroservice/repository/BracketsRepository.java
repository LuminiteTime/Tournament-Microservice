package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Brackets;

public interface BracketsRepository extends JpaRepository<Brackets, Long> {
}
