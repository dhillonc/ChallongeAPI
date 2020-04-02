import api.challonge.Challonge;
import api.challonge.GameType;

import java.text.DecimalFormat;

public class Main {


    static DecimalFormat mf = new DecimalFormat("#####.##");
    public static void main(String[] args){
        Challonge challonge = new Challonge("api-key", "IAMRJ", "testing12324533", "IAMRJ's Tournament", "fun 1v1 tournament");
        Challonge challonge = new Challonge("api_key", "IAMRJ", mf.format(System.currentTimeMillis() * 2) + "", "IAMRJ's Tournament", "fun 1v1 tournament", GameType.DOUBLE);

        for (int i = 0; i<39; i++){
            challonge.getParticipants().add("test" + i);
        }
        System.out.println(challonge.post());
        System.out.println(challonge.addParticpants());
        System.out.println(challonge.randomize());
        System.out.println(challonge.start());
        System.out.println(challonge.getUrl());
    }
}
