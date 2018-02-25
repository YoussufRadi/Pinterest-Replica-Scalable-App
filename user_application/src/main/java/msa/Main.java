package msa;

import msa.postgresql.DatabaseController;

import java.util.UUID;

public class Main {

    public static void main (String[]argv){

        DatabaseController dbcontroller = new DatabaseController();
        UUID uid = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f655");
        UUID uid2 = UUID.fromString("85c05c80-564a-425e-9a5b-aec519a9f778");

        UUID empID1 = dbcontroller.
                addUser(
                        "mostafa","MOE",
                        "alisouidan@gmail.com","b8=M3Y",
                        true);

        UUID empID2 = dbcontroller.
                addUser(
                        "yomna","MOE",
                        "alisouidan@gmail.com","b8=M3Y",
                        true);

        UUID emp = dbcontroller.
                addUser(
                        "yomna","MOE",
                        "alisouidan@gmail.com","b8=M3Y",
                        true);

          dbcontroller.blockUser(empID1,empID2);
         //dbcontroller.unblockUser(UUID.fromString("8524ca5b-b40c-40f3-ac36-fe686b84c880"),UUID.fromString("d4f46c79-dd03-4c6d-b67e-7116b2cdc02e"));
        //dbcontroller.followUser(empID1,empID2);
       // dbcontroller.unfollowUser(UUID.fromString("7ee19aa8-a084-4bc9-8e0b-16df2d264ce8"),UUID.fromString("c77924f2-5d69-4676-97d9-8a1549034925"));
        dbcontroller.followCategories(empID1,uid2);

        dbcontroller.listUsers();





    }
}
