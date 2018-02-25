package msa;

import msa.postgresql.DatabaseController;

import java.util.UUID;

public class Main {

    public static void main (String[]argv){

        DatabaseController dbcontroller = new DatabaseController();
        UUID uid = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f655");
        UUID uid2 = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f778");

        UUID userID1 = dbcontroller.
                addUser(
                        "mostafa","MOE",
                        "alison@gmail.com","b8=M3Y",
                        true);

        UUID userID2 = dbcontroller.
                addUser(
                        "yomna","wael",
                        "yomna@gmail.com","b8=M3Y",
                        true);

        UUID userID3 = dbcontroller.
                addUser(
                        "sohaila","eltarabily",
                        "sohaila@gmail.com","b8=M3Y",
                        true);

          dbcontroller.blockUser(userID1,userID2);
         //dbcontroller.unblockUser(UUID.fromString("8524ca5b-b40c-40f3-ac36-fe686b84c880"),UUID.fromString("d4f46c79-dd03-4c6d-b67e-7116b2cdc02e"));
        //dbcontroller.followUser(empID1,empID2);
       // dbcontroller.unfollowUser(UUID.fromString("7ee19aa8-a084-4bc9-8e0b-16df2d264ce8"),UUID.fromString("c77924f2-5d69-4676-97d9-8a1549034925"));
        dbcontroller.followCategories(userID1,uid2);

        dbcontroller.listUsers();





    }
}
