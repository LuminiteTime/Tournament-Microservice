package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
//    Optional<Player> findByName(String name);
}
