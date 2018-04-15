package Cache;

import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.File;
import java.io.IOException;

public class RedisConf {

    private RedissonClient client;

    private  RLiveObjectService service;

    public RedissonClient getClient() {
        return client;
    }


    public RLiveObjectService getService() {
        return service;
    }




    public RedisConf() throws IOException {
        client = Redisson.create();
        Config config = Config.fromJSON(new File("singleNodeConfig.json"));
        service = client.getLiveObjectService();

    }

    public static void main(String[] argv) throws IOException {

        RedisConf conf = new RedisConf();
//        DatabaseController cont = new DatabaseController();
//        RLiveObjectService service = conf.client.getLiveObjectService();
//
//        User u = new User("AhmedRedis",
//                "ZaherRedis",
//                "ahmedzher@gmail.com",
//                "password",false
//        );
//
//        UUID category1 = UUID.fromString("795baeca-f012-49ba-91a4-dd0ae0dbba97");
//        UUID category2 = UUID.fromString("d029f932-df07-4ca4-b368-503f5a4e1712");
//
//
//        UUID id = cont.addUser(u);
//
//        cont.followCategories(id,category1);
//        cont.followCategories(id,category1);
//        cont.followCategories(id,category2);
//
//
//        User user = cont.getUserWithProfileById(id);
//
//        String message = new Gson().toJson(user);
//
//        Gson gson = new GsonBuilder().create();
//        UserLiveObject userLive = gson.fromJson(message, UserLiveObject.class);
//
//        userLive = service.persist(userLive);
//
//        UserLiveObject returnuserLive
//                = service.get(UserLiveObject.class, id.toString());
//
//        System.out.println(returnuserLive.getUserCat().toString());
//*/

       /* UserLiveObject ulo = new UserLiveObject(id.toString(),user);

        ulo = service.persist(ulo);

        UserLiveObject uloOut = service.get(UserLiveObject.class,id.toString());

        System.out.println(uloOut.getUser().getUserCat().toString());

*/
    }
}
