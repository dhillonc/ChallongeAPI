import api.challonge.Challonge;
import api.challonge.GameType;

import java.text.DecimalFormat;

public class Main {


    static DecimalFormat mf = new DecimalFormat("#####.##");

    public static void main(String[] args) {
        Challonge challonge = new Challonge("***********", "NightShadePvP", mf.format(System.currentTimeMillis() * 2) + "", "IAMRJ's Tournament", "fun 1v1 tournament", GameType.SINGLE);

        for (int i = 0; i < 30; i++) {
            challonge.getParticipants().add("test" + i);
        }

        challonge.post();
        challonge.addParticpants();
        System.out.println(challonge.start());
        System.out.println(challonge.indexMatches());
        System.out.println(challonge.getMatch(1));
        System.out.println(challonge.getMatchParticipants(1).toString());
        System.out.println(challonge.getUrl());
    }
}
