import api.challonge.Challonge;
import api.challonge.GameType;

import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class Main {


    static DecimalFormat mf = new DecimalFormat("#####.##");

    public static void main(String[] args) {
        try{
            Challonge challonge = new Challonge("PC4Qd6rMkNxBn4v7KKLTXJRGyDwlgOOdd6gnArUp", "NightShadePvP", mf.format(System.currentTimeMillis() * 2) + "", "IAMRJ's Tournament", "fun 1v1 tournament", GameType.SINGLE);

            for (int i = 0; i < 10; i++) {
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
            challonge.start().get();
            challonge.indexMatches().get();

            for (int i = 1; i < Challonge.getMatchIds().keySet().size() +1; i++) {
                challonge.markAsUnderway(i).get();
                Thread.sleep(2000);
                challonge.unMarkAsUnderway(i).get();
                try {
                    for (Integer matchPart : challonge.getMatchParticipants(i).get()) {
                        winner = matchPart;
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println(challonge.getNameFromId(winner));
                System.out.println(challonge.updateMatch(i, winner).get());
            }

            challonge.end().get();
            System.out.println("Champion: " + challonge.getChampion());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
