package api.challonge;


import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Getter
@Setter
public class Challonge {

    public List<String> participants;

    private String api, username, url, name, description;
    private GameType gameType;
    private HttpResponse<JsonNode> response;

    public HashMap<String, Integer> partId = new HashMap<>();
    public HashMap<Integer, String> matchIds = new LinkedHashMap<>();

    public Challonge(String api, String username, String url, String name, String description, GameType gameType) {
        this.api = api;
        this.username = username;
        this.url = url;
        this.name = name;
        this.description = description;
        this.gameType = gameType;
        participants = new ArrayList<>();
    }


    /**
     * @param id id of the user you want to get
     * @return String name of the user based of the ID
     */
    public String getNameFromId(Integer id) {
        for (Map.Entry<String, Integer> entry : partId.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Integer getMatchIdFromChallongeId(String id){
        for (Map.Entry<Integer, String> entry : matchIds.entrySet()){
            if(entry.getValue().equals(id)){
                return entry.getKey();
            }
        }

        return -1;
    }

    public CompletableFuture<Boolean> post() {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments.json")
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .field("tournament[name]", name)
                    .field("tournament[url]", url)
                    .field("tournament[tournament_type]", gameType.getName())
                    .field("tournament[description]", description)
                    .asJson();
            this.response = response;
            return response.getStatus() == 200;

        });
    }

    public CompletableFuture<Boolean> addParticpants() {
        return supplyAsync(() -> {
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
                    int id = value.getInt("id");
                    String name = value.getString("name");
                    partId.put(name, id);
                    System.out.println(name + id);
                }
            }
            this.response = response;
            return response.getStatus() == 200;

        });
    }

    public CompletableFuture<Boolean> indexMatches() {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.get("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches.json".replace("{tournament}", url))
                    .header("accept", "application/json")
                    .queryString("api_key", api)
                    .asJson();

            int m = 1;
            JSONArray jsonObject = response.getBody().getArray();
            for (int i = 0; i < jsonObject.length(); i++) {

                JSONObject object = jsonObject.getJSONObject(i);
                Iterator keys = object.keys();
                while (keys.hasNext()) {
                    Object key = keys.next();
                    JSONObject value = object.getJSONObject((String) key);
                    Integer id = value.getInt("id");
                    this.matchIds.put(m, String.valueOf(id));
                }
                m++;
            }
            return response.getStatus() == 200;
        });
    }

    /**
     * gets the URL of the match
     *
     * @return match URL
     */
    public String getUrl() {
        return "https://challonge.com/" + url;
    }

    public CompletableFuture<Boolean> start() {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/start.json".replace("{tournament}", url))
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .asJson();
            return response.getStatus() == 200;
        });
    }

    /**
     * ends the tournament and archives the match post
     *
     * @return
     */
    public CompletableFuture<Boolean> end() {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/finalize.json".replace("{tournament}", url))
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .asJson();
            return response.getStatus() == 200;
        });
    }

    /**
     * Randomizes the seeds
     *
     * @return if successful
     */
    public CompletableFuture<Boolean> randomize() {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/randomize.json".replace("{tournament}", url))
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .asJson();
            return response.getStatus() == 200;
        });
    }

    public CompletableFuture<JSONArray> getMatch(int id) {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.get("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                    replace("{tournament}", url)
                    .replace("{match_id}", matchIds.get(id)))
                    .header("accept", "application/json")
                    .queryString("api_key", api)
                    .asJson();
            return response.getBody().getArray();
        });
    }

    public CompletableFuture<Boolean> updateMatch(int id, String name) {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.put("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                    replace("{tournament}", url)
                    .replace("{match_id}", matchIds.get(id)))
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .field("match[scores_csv]", "1-0")
                    .field("match[winner_id]", String.valueOf(partId.get(name)))
                    .asJson();

            return response.getStatus() == 200;
        });
    }

    public CompletableFuture<Boolean> updateMatch(int id, int winneriD) {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.put("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                    replace("{tournament}", url)
                    .replace("{match_id}", matchIds.get(id)))
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .field("match[scores_csv]", "1-0")
                    .field("match[winner_id]", String.valueOf(winneriD))
                    .asJson();

            return response.getStatus() == 200;
        });
    }

    public CompletableFuture<Boolean> markAsUnderway(int matchId) {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}/mark_as_underway.json".
                    replace("{tournament}", url)
                    .replace("{match_id}", matchIds.get(matchId)))
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .asJson();

            return response.getStatus() == 200;
        });
    }

    public CompletableFuture<Boolean> unMarkAsUnderway(int matchId) {
        return supplyAsync(() -> {
            HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}/unmark_as_underway.json".
                    replace("{tournament}", url)
                    .replace("{match_id}", matchIds.get(matchId)))
                    .header("accept", "application/json")
                    .field("api_key", api)
                    .asJson();

            return response.getStatus() == 200;
        });
    }

    /**
     * @param matchId
     * @return array of particpants in the match
     */

    public CompletableFuture<Integer[]> getMatchParticipants(int matchId) {
        return supplyAsync(() -> {
            JSONObject match = null;
            try {
                match = getMatch(matchId).get().getJSONObject(0).getJSONObject("match");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return new Integer[]{
                    (match.getInt("player1_id")),
                    match.getInt("player2_id")
            };
        });
    }

    public CompletableFuture<HashSet<JSONObject>> getMatchesByRound(int round){
        return supplyAsync(() -> {
            HashSet<JSONObject> matches = new HashSet<>();
            for (int id : this.matchIds.keySet()){

                try {
                    JSONObject obj = getMatch(id).get().getJSONObject(0).getJSONObject("match");
                    if(obj.getInt("round") == round){
                        matches.add(obj);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    break;
                }
            }

            return matches;
        });


    }

    public CompletableFuture<Integer> getRounds() {
        return supplyAsync(() -> {
            List<Map.Entry<Integer, String>> entryList =
                    new ArrayList<>(matchIds.entrySet());
            Map.Entry<Integer, String> first =
                    entryList.get(0);

            try {
                return getMatch(first.getKey()).get().getJSONObject(0).getJSONObject("match").getInt("round");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return -1;
            }

        });
    }

    public HashMap<String, Integer> getPartId() {
        return partId;
    }

    public HashMap<Integer, String> getMatchIds() {
        return matchIds;
    }

    public String getChampion() {
        int id;
        try {
            id = getMatch(matchIds.size()).get().getJSONObject(0).getJSONObject("match").getInt("winner_id");
        } catch (InterruptedException e) {
            id = -1;
            e.printStackTrace();
        } catch (ExecutionException e) {
            id = -1;
            e.printStackTrace();
        }

        if (id < 0) {
            throw new IllegalArgumentException("That game has no winner!");
        }

        if (getNameFromId(id) != null) {
            return getNameFromId(id);
        }

        return null;
    }
}