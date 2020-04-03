import api.challonge.Challonge;
import api.challonge.GameType;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class Main {


    static DecimalFormat mf = new DecimalFormat("#####.##");

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Challonge challonge = new Challonge("iBxWp4qs86txMquXmRGd3nVJ3zlIYUJGrNYfoxnv", "IAMRJ", mf.format(System.currentTimeMillis() * 2) + "", "IAMRJ's Tournament", "fun 1v1 tournament", GameType.DOUBLE);

        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0){
                challonge.getParticipants().add("Nightshade" + i);
            }else{
                challonge.getParticipants().add("Scenarios" + i);
            }
        }

        int winner = 0;
        challonge.post().get();
        System.out.println(challonge.getUrl());
        challonge.addParticpants().get();
        challonge.randomize().get();
        challonge.start().get();
        challonge.indexMatches().get();

        for (int i = 1; i < challonge.getMatchIds().keySet().size()+1; i++) {
            challonge.markAsUnderway(i).get();
            Thread.sleep(2000);
            challonge.unMarkAsUnderway(i).get();
            for (Integer matchPart : challonge.getMatchParticipants(i).get()) {
                winner = matchPart;
            }
            System.out.println(challonge.getNameFromId(winner));
            System.out.println(challonge.updateMatch(i, winner).get());
        }

        challonge.end();
    }
}
