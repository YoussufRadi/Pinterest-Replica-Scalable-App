package TestService;


import org.json.simple.JSONObject;

public class UserModel {
    //example
    public static String getUser(String tag) {
        JSONObject userObject = new JSONObject();
        userObject.put("user_name","test");
        userObject.put("email","test@test.com");
        userObject.put("date_of_birth","1/1/2001");
        userObject.put("gender","true");
        doWork(tag);
        userObject.put("tag", tag);
        return userObject.toString();
    }

    private static void doWork(String task) {
        for (char ch : task.toCharArray()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }

        }
    }

    }
