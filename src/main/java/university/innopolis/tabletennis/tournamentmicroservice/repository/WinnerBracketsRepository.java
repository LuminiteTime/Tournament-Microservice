package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.WinnerBrackets;

public interface WinnerBracketsRepository extends JpaRepository<WinnerBrackets, Long> {
}
