import api.challonge.Challonge;

public class Main {



    public static void main(String[] args){
        Challonge challonge = new Challonge("api-key", "IAMRJ", "testing12324533", "IAMRJ's Tournament", "fun 1v1 tournament");

        for (int i = 0; i<39; i++){
            challonge.getParticipants().add("test" + i);
        }
        challonge.post();
        challonge.addParticpants();
    }
}
