package university.innopolis.tabletennis.tournamentmicroservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer rating;
    private Integer indexInTable;
    private Integer tableIndex;
    private Boolean isPlaying;

    public Player() {
        this.isPlaying = false;
    }

    public Player(String name, Integer rating)
    {
        this.name = name;
        this.rating = rating;
        this.isPlaying = false;
    }
}
