package api.challonge;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Challonge {

    public List<String> participants;

    private String api, username, url, name, description;
    private GameType gameType = GameType.DOUBLE;
    private HttpResponse<JsonNode> response;

    public Challonge(String api, String username, String url, String name, String description) {
        this.api = api;
        this.username = username;
        this.url = url;
        this.name = name;
        this.description = description;
        participants = new ArrayList<>();
    }
    public String post(){
        HttpResponse<JsonNode> response  = Unirest.post("https://" +username + ":" + api +"@api.challonge.com/v1/tournaments.json")
                .header("accept", "application/json")
                .field("api_key", api)
                .field("tournament[name]", name)
                .field("tournament[url]", url)
                .asJson();
        this.response = response;
        return response.getBody().toPrettyString();
    }


}
