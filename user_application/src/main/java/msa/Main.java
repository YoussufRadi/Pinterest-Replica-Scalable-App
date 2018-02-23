package msa;

import msa.postgresql.DatabaseController;

import java.util.UUID;

public class Main {

    public static void main (String[]argv){

        DatabaseController dbcontroller = new DatabaseController();
        UUID empID1 = dbcontroller.
                addUser(
                        "KHALED","MOE",
                        "alisouidan@gmail.com","b8=M3Y",
                        true);
        UUID empID2 = dbcontroller.
                addUser(
                        "Zaher","Tarek",
                        "zaher@gmail.com","b8=M3Y",
                        true);

        UUID empID3 = dbcontroller.
                addUser(
                        "Youmnazzzzzzzzzzzz","Wael",
                        "yumyumxo95@gmail.com","b8=M3Y",
                        false);

        UUID empID4 = dbcontroller.
                addUser(
                        "John","Torry",
                        "Johntorry@gmail.com","b8=M3Y",
                        true);



        dbcontroller.listUsers();





    }
}
