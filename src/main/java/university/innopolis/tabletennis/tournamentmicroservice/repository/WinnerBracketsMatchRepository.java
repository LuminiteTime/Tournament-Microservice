package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.WinnerBracketsMatch;

public interface WinnerBracketsMatchRepository extends JpaRepository<WinnerBracketsMatch, Long> {
}
