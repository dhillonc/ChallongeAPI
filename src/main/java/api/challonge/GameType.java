package api.challonge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {
    SINGLE("single elimination"),
    DOUBLE("double elimination");


    private String name;
}
