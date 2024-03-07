package university.innopolis.tabletennis.tournamentmicroservice.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import university.innopolis.tabletennis.tournamentmicroservice.entity.Match;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    default List<Match> findAllSortedMatches() {
        return this.findAll(
                Sort.by(Sort.Direction.ASC, "gameTableIndex")
                        .and(Sort.by(Sort.Direction.ASC, "roundIndex"))
        );
    }

    default List<Match> findAvailableMatches() {
        List<Match> allMatches = this.findAllSortedMatches();
        List<Match> availableMatches = new ArrayList<>();
        for (Match match: allMatches) {
            if (match.getFirstPlayer().getIsPlaying() || match.getSecondPlayer().getIsPlaying()
                    || match.getIsCompleted() || match.getIsBeingPlayed()) {
                continue;
            }
            availableMatches.add(match);
        }
        return availableMatches;
    }
}
