import api.challonge.Challonge;
import api.challonge.GameType;

import java.text.DecimalFormat;

public class Main {


    static DecimalFormat mf = new DecimalFormat("#####.##");

    public static void main(String[] args) throws InterruptedException {
        Challonge challonge = new Challonge("", "IAMRJ", mf.format(System.currentTimeMillis() * 2) + "", "IAMRJ's Tournament", "fun 1v1 tournament", GameType.DOUBLE);

        for (int i = 0; i < 201; i++) {
            if (i % 2 == 0){
                challonge.getParticipants().add("Nightshade" + i);
            }else{
                challonge.getParticipants().add("Scenarios" + i);
            }
        }

        int winner = 0;
        challonge.post();
        System.out.println(challonge.getUrl());
        challonge.addParticpants();
        challonge.start();
        challonge.indexMatches();

        for (int i = 1; i < challonge.getMatchIds().keySet().size() +1; i++) {
            challonge.markAsUnderway(i);
            Thread.sleep(2000);
            challonge.unMarkAsUnderway(i);
            for (Integer matchPart : challonge.getMatchParticipants(i)) {
                winner = matchPart;
            }
            System.out.println(challonge.getNameFromId(winner));
            System.out.println(challonge.updateMatch(i, winner));
        }

        challonge.end();
    }
}
