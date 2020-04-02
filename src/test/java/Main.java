import api.challonge.Challonge;

public class Main {



    public static void main(String[] args){
        Challonge challonge = new Challonge("****", "IAMRJ", "testing1234533", "IAMRJ's Tournament", "fun 1v1 tournament");


        System.out.println(challonge.post());
    }
}
