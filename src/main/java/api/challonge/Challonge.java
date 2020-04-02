package api.challonge;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Challonge {

    public List<String> participants;

    private String api, username, url, name, description;
    private GameType gameType;
    private HttpResponse<JsonNode> response;

    private HashMap<String, String> partId;
    private HashMap<Integer, String> matchIds;

    public Challonge(String api, String username, String url, String name, String description, GameType gameType) {
        this.api = api;
        this.username = username;
        this.url = url;
        this.name = name;
        this.description = description;
        this.gameType = gameType;
        partId = new HashMap<>();
        matchIds = new HashMap<>();
        participants = new ArrayList<>();
    }


    public String getNameFromId(String id) {
        for (Map.Entry<String, String> entry : partId.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String post() {
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments.json")
                .header("accept", "application/json")
                .field("api_key", api)
                .field("tournament[name]", name)
                .field("tournament[url]", url)
                .field("tournament[tournament_type]", gameType.getName())
                .field("tournament[description]", description)
                .asJson();
        this.response = response;
        return response.getBody().toPrettyString();
    }

    public boolean addParticpants() {
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/participants/bulk_add.json".replace("{tournament}", url))
                .header("accept", "application/json")
                .field("api_key", api)
                .field("participants[][name]", participants)
                .asJson();

        JSONArray jsonObject = response.getBody().getArray();
        for (int i = 0; i < jsonObject.length(); i++) {

            JSONObject object = jsonObject.getJSONObject(i);
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                JSONObject value = object.getJSONObject((String) key);
                String id = value.getString("id");
                String name = value.getString("name");
                partId.put(name, id);
            }
        }
        return response.getStatus() == 200;
    }


    public void test() {
        for (String s : partId.keySet()) {
            System.out.println("Name: " + s + "ID: " + partId.get(s));
        }
    }

    /*
    returns 404 not found, unsure why
     */
    public boolean indexMatches() {
        HttpResponse<JsonNode> response = Unirest.get("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches.json".replace("{tournament}", url))
                .header("accept", "application/json")
                .queryString("api_key", api)
                .asJson();

        System.out.println(response.getBody().toPrettyString());
        int m = 1;
        JSONArray jsonObject = response.getBody().getArray();
        for (int i = 0; i < jsonObject.length(); i++) {

            JSONObject object = jsonObject.getJSONObject(i);
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                JSONObject value = object.getJSONObject((String) key);
                String id = value.getString("id");
                this.matchIds.put(m, id);
            }
        }
        return response.getStatus() == 200;
    }

    public String getUrl() {
        return "https://challonge.com/" + url;
    }

    public boolean start() {
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/start.json".replace("{tournament}", url))
                .header("accept", "application/json")
                .field("api_key", api)
                .asJson();
        if (response.getStatus() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public boolean end() {
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/finalize.json".replace("{tournament}", url))
                .header("accept", "application/json")
                .field("api_key", api)
                .asJson();
        if (response.getStatus() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public boolean randomize() {
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/participants/randomize.json".replace("{tournament}", url))
                .header("accept", "application/json")
                .field("api_key", api)
                .asJson();
        if (response.getStatus() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public JSONArray getMatch(int id) {
        HttpResponse<JsonNode> response = Unirest.get("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                replace("{tournament}", url)
                .replace("{match_id}", matchIds.get(id)))
                .header("accept", "application/json")
                .queryString("api_key", api)
                .asJson();
        return response.getBody().getArray();
    }


    public boolean updateMatch(int matchId, String name) {
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                replace("{tournament}", url)
                .replace("{match_id}", matchIds.get(matchId)))
                .header("accept", "application/json")
                .field("api_key", api)
                .field("match[winner_id]", partId.get(name))
                .asJson();

        return false;
    }

    public String[] getMatchParticipants(int matchId) {
        JSONObject match = getMatch(matchId).getJSONObject(0).getJSONObject("match");

        return new String[]{
                partId.get((String) match.get("player1_id")),
                partId.get((String) match.get("player2_id"))
        };
    }

    public String getRound(int matchId) {
        JSONObject match = getMatch(matchId).getJSONObject(0).getJSONObject("match");
        return (String) match.get("round");
    }

    public Set<JSONObject> getMatches(String participantName) {
        HashSet<JSONObject> matches = new HashSet<>();

        JSONArray array;
        String[] participants;
        for (int i : this.matchIds.keySet()) {
            array = getMatch(i);
            participants = getMatchParticipants(i);
            if(Arrays.stream(participants).anyMatch(participantName::equalsIgnoreCase)){
                matches.add(array.getJSONObject(0).getJSONObject("match"));
            }
        }

        return matches;
    }
}
