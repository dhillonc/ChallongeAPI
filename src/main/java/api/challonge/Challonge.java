package api.challonge;

import api.challonge.GameType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

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

    private HashMap<String, Integer> partId;
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


    /**
     *
     * @param id id of the user you want to get
     * @return String name of the user based of the ID
     */
    public String getNameFromId(Integer id){
        for (Map.Entry<String, Integer> entry : partId.entrySet()) {
            if (entry.getValue().equals(id)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * initial method for posting the match
     * @return
     */
  /*  public boolean post3() {
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
    }*/


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

    /**
     *
     * @return if successful
     */
   /* public boolean addParticpants() {
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
            }
        }
        return response.getStatus() == 200;
    }*/


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
                }
            }
            this.response = response;
            return response.getStatus() == 200;

        });
    }



    /**
     *      Adds matches to HashMap based of their match ID starting at 1
     * @return if successful
     * */
    /*public boolean indexMatches() {
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
                String id = value.getString("id");
                this.matchIds.put(m, id);
            }
            m++;
        }
        return response.getStatus() == 200;
    }*/


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
                    String id = value.getString("id");
                    this.matchIds.put(m, id);
                }
                m++;
            }
            return response.getStatus() == 200;
        });
    }

    /**
     * gets the URL of the match
     * @return match URL
     */
    public String getUrl() {
        return "https://challonge.com/" + url;
    }

    /**
     * Sets the tournament as started
     * @return if successful
     */
    /*public boolean start() {
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/start.json".replace("{tournament}", url))
                .header("accept", "application/json")
                .field("api_key", api)
                .asJson();
        if (response.getStatus() == 200) {
            return true;
        } else {
            return false;
        }
    }*/


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

    /**
     *
     * @param id match id
     * @return the jsonarray of a match
     */
  /*  public JSONArray getMatch(int id) {
        HttpResponse<JsonNode> response = Unirest.get("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                replace("{tournament}", url)
                .replace("{match_id}", matchIds.get(id)))
                .header("accept", "application/json")
                .queryString("api_key", api)
                .asJson();
        return response.getBody().getArray();
    }*/


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

   /* public boolean updateMatch(int matchId, String name) {
        HttpResponse<JsonNode> response = Unirest.put("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                replace("{tournament}", url)
                .replace("{match_id}", matchIds.get(matchId)))
                .header("accept", "application/json")
                .field("api_key", api)
                .field("match[scores_csv]", "1-0")
                .field("match[winner_id]", String.valueOf(partId.get(name)))
                .asJson();

        return response.getStatus() == 200;
    }*/

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
  /*  public boolean updateMatch(int matchId, int winnerId) {
        HttpResponse<JsonNode> response = Unirest.put("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}.json".
                replace("{tournament}", url)
                .replace("{match_id}", matchIds.get(matchId)))
                .header("accept", "application/json")
                .field("api_key", api)
                .field("match[scores_csv]", "1-0")
                .field("match[winner_id]", String.valueOf(winnerId))
                .asJson();

        return response.getStatus() == 200;
    }*/

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


    /*public boolean markAsUnderway(int matchId){
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}/mark_as_underway.json".
                replace("{tournament}", url)
                .replace("{match_id}", matchIds.get(matchId)))
                .header("accept", "application/json")
                .field("api_key", api)
                .asJson();

        return response.getStatus() == 200;
    }
    public boolean unMarkAsUnderway(int matchId){
        HttpResponse<JsonNode> response = Unirest.post("https://" + username + ":" + api + "@api.challonge.com/v1/tournaments/{tournament}/matches/{match_id}/unmark_as_underway.json".
                replace("{tournament}", url)
                .replace("{match_id}", matchIds.get(matchId)))
                .header("accept", "application/json")
                .field("api_key", api)
                .asJson();

        return response.getStatus() == 200;
    }
*/


    /**
     *
     * @param matchId
     * @return array of particpants in the match
     */

    public CompletableFuture<Integer[]> getMatchParticipants(int matchId) {
        return supplyAsync(() -> {
            JSONObject match = null;
            try {
                match = getMatch(matchId).get().getJSONObject(0).getJSONObject("match");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return new Integer[]{
                    (match.getInt("player1_id")),
                    match.getInt("player2_id")
            };
        });
    }


  /*  public Integer[] getMatchParticipants(int matchId) throws ExecutionException, InterruptedException {
        JSONObject match = getMatch(matchId).get().getJSONObject(0).getJSONObject("match");

        return new Integer[]{
                (match.getInt("player1_id")),
                match.getInt("player2_id")
        };
    }*/

/*    public String getRound(int matchId) {
        JSONObject match = getMatch(matchId).getJSONObject(0).getJSONObject("match");
        return (String) match.get("round");
    }*/

  /* public ArrayList<JSONObject> getMatches(String participantName) {
        ArrayList<JSONObject> matches = new ArrayList<>();

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
*/
}
