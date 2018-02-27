package msa;

import msa.messagequeue.QHandler;
import msa.postgresql.DatabaseController;

import java.io.IOException;
import java.util.UUID;

public class Main {

    public static void main (String[]argv) throws IOException {

        DatabaseController dbcontroller = new DatabaseController();
        QHandler q =new QHandler();
        UUID uid = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f655");
        UUID uid2 = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f778");

        UUID userID1 = q.
                addUser(
                        "mostafaKHALEDEGE","MOE","username",
                        "alison@gmail.com","b8=M3Y",
                        true,25);
        q.updateUser(userID1,"ahmed","khaled",
                "passwrd","username",13,true);







    }
}
