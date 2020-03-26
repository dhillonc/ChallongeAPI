package api.challonge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Challonge {

    public List<String> participants = new ArrayList<>();

    private String api, username, url, name, description;
    private GameType gameType;




}
