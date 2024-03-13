package university.innopolis.tabletennis.tournamentmicroservice.requestbody;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IdListRequestBody {

    private List<Long> idList;

    public IdListRequestBody() {
        this.idList = new ArrayList<>();
    }
}
