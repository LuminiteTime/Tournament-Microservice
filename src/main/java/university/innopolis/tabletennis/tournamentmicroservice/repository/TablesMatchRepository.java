package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.TablesMatch;

public interface TablesMatchRepository extends JpaRepository<TablesMatch, Long> {
}
