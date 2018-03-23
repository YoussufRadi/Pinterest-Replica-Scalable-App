package msa.messagequeue;


import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;
import msa.userservice.*;
import msa.userservice.Command;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "user-request";
    private static final String RPC_RESPONSE_QUEUE = "user-response";

    static ExecutorService executor = Executors.newFixedThreadPool(15);
    UserCacheController UserCacheController;

    public RPCServer() throws IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        UserCacheController = new UserCacheController();

        Connection connection = null;
        try {
            connection      = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.queueDeclare(RPC_RESPONSE_QUEUE , false, false, false, null);


            channel.basicQos(1);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();

                    String response = "";

                    try {
                        String message = new String(body, "UTF-8");
                        JSONParser parser = new JSONParser();
                        JSONObject command = (JSONObject) parser.parse(message);
                        String className = "msa.userservice." + (String)command.get("command");
                        Class com = Class.forName(className);
                        Command cmd = (Command) com.newInstance();

                        HashMap<String, Object> init2 = new HashMap<String, Object>();
                        init2.put("channel", channel);
                        init2.put("properties", properties);
                        init2.put("replyProps", replyProps);
                        init2.put("envelope", envelope);
                        init2.put("body", message);
                        init2.put("UserCacheController", UserCacheController);
                        cmd.init(init2);
                        executor.submit(cmd);






                    }



                    catch (RuntimeException e){
                        System.out.println(" [.] " + e.toString());
                    }  catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();

                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized(consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {}
        }
    }



   /* public  Object handleMessage(Message msg){

        String method = msg.getMethod();
        User payload = msg.getPayload();
        UserLiveObject live;



        switch (method){
            case "SignIn" :
                 live = UserCacheController.signIn(msg.getPayload().getEmail(),
                        msg.getPayload().getPassword());
                System.out.println(live.getId());
                String message = new Gson().toJson(live);
                return message;

            case "SignUp" :
                System.out.println(payload.getEmail());
                System.out.println(payload.getAge());

                UUID id  = UserCacheController.addUser(payload.getFirstName(),payload.getLastName(),payload.getUsername(),
                        payload.getEmail(),payload.getPassword(),payload.isGender(),payload.getAge());
                System.out.println("ID FROM SERVER");
                System.out.println(id);
                if(id == null)
                    return "User Already Exists";

                return id;

            case "updateUser" :
                UserCacheController.updateUser(payload.getId(),payload.getFirstName(),
                        payload.getLastName(),payload.getPassword(),
                        payload.getUsername(),payload.getAge(),payload.isGender());

            case "LikePhoto" :
                System.out.println(UUID.fromString(msg.getPhotoId()));
                boolean liveFlag= UserCacheController.likePhotos(msg.getPayload().getId(),
                        UUID.fromString(msg.getPhotoId()));
                // return message;

                return  liveFlag;

            case "unlikePhoto" :
                System.out.println(UUID.fromString(msg.getPhotoId()));
                boolean unlikePhotoFlag= UserCacheController.unlikePhotos(msg.getPayload().getId(),
                        UUID.fromString(msg.getPhotoId()));
                // return message;

                return  unlikePhotoFlag;

            case "dislikePhoto" :
                System.out.println(UUID.fromString(msg.getPhotoId()));
                boolean dislikePhotoFlag= UserCacheController.dislikePhotos(msg.getPayload().getId(),
                        UUID.fromString(msg.getPhotoId()));
                // return message;

                return  dislikePhotoFlag;

            case "undislikePhoto" :
                System.out.println(UUID.fromString(msg.getPhotoId()));
                boolean undislikePhotoFlag= UserCacheController.undislikePhotos(msg.getPayload().getId(),
                        UUID.fromString(msg.getPhotoId()));
                // return message;

                return  undislikePhotoFlag;
            case "addPin" :
                boolean addPinFlag= UserCacheController.addPin(msg.getPayload().getId(),
                        UUID.fromString(msg.getPinId()));
                // return message;

                return  addPinFlag;
            case "removePin" :
                boolean removePinFlag= UserCacheController.removePin(msg.getPayload().getId(),
                        UUID.fromString(msg.getPinId()));
                // return message;

                return  removePinFlag;

            case "followUser" :
                boolean followUserFlag= UserCacheController.followUser(msg.getPayload().getId(),
                        UUID.fromString(msg.getOtherUserId()));
                // return message;

                return  followUserFlag;

            case "unfollowUser" :
                boolean unFollowUserFlag= UserCacheController.unfollowUser(msg.getPayload().getId(),
                        UUID.fromString(msg.getOtherUserId()));
                // return message;

                return  unFollowUserFlag;


        case "blockUser" :
        boolean blockUserFlag= qHandler.blockUser(msg.getPayload().getId(),
                UUID.fromString(msg.getOtherUserId()));
        // return message;

        return  blockUserFlag;

        case "unblockUser" :
        boolean unblockUserFlag= qHandler.UnblockUser(msg.getPayload().getId(),
                UUID.fromString(msg.getOtherUserId()));
        // return message;

        return  unblockUserFlag;

        case "followHashtags" :
        boolean followHashtagFlag= qHandler.followHashtags(msg.getPayload().getId(),
                UUID.fromString(msg.getHashtagId()));
        // return message;

        return  followHashtagFlag;

        case "unfollowHashtags" :
        boolean unfollowHashtagFlag= qHandler.unfollowHashtags(msg.getPayload().getId(),
                UUID.fromString(msg.getHashtagId()));
        // return message;

        return  unfollowHashtagFlag;

        case "followCategories" :
        boolean followCategoriesFlag= qHandler.followCategories(msg.getPayload().getId(),
                UUID.fromString(msg.getCategoryId()));
        // return message;

        return  followCategoriesFlag;

        case "unfollowCategories" :
        boolean unfollowCategoriesFlag= qHandler.unfollowCategories(msg.getPayload().getId(),
                UUID.fromString(msg.getCategoryId()));
        // return message;

        return  unfollowCategoriesFlag;
        case "addBoard" :
        boolean addBoardId= qHandler.addBoard(msg.getPayload().getId(),
                UUID.fromString(msg.getBoardId()));
        // return message;

        return  addBoardId;

        case "removeBoard" :
        boolean removeBoardFlag= qHandler.removeBoard(msg.getPayload().getId(),
                UUID.fromString(msg.getBoardId()));
        // return message;

        return  removeBoardFlag;

    }

        return null;
    }

*/




    public static void main(String[] argv) throws IOException {
    RPCServer rpcServer = new RPCServer();

    }

}



