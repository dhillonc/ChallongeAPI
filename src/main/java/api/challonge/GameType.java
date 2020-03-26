package api.challonge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {
    SINGLE("single elm"),
    DOUBLE("double elm");


    private String name;
}
