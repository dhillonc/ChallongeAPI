import api.challonge.Challonge;
import api.challonge.GameType;

import java.text.DecimalFormat;

public class Main {


    static DecimalFormat mf = new DecimalFormat("#####.##");

    public static void main(String[] args) throws InterruptedException {
        Challonge challonge = new Challonge("", "IAMRJ", mf.format(System.currentTimeMillis() * 2) + "", "IAMRJ's Tournament", "fun 1v1 tournament", GameType.SINGLE);

        for (int i = 0; i < 39; i++) {
            challonge.getParticipants().add("test" + i);
        }


        challonge.post();
        challonge.addParticpants();
        challonge.start();
        challonge.indexMatches();
        System.out.println(challonge.getMatch(1));
        for (Integer participant : challonge.getMatchParticipants(1)) {
            System.out.println(challonge.getNameFromId(participant));
        }
        System.out.println(challonge.getUrl());
        challonge.updateMatch(1, "test31");
    }
}
