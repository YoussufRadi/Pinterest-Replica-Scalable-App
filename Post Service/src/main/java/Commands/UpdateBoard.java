package Commands;


import Database.ArangoInstance;
import Interface.Command;
import Models.BoardDBObject;
import Models.BoardLiveObject;
import Models.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.JSONObject;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.HashMap;


public class UpdateBoard extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        RLiveObjectService RLiveObjectService = (RLiveObjectService)
                parameters.get("RLiveObjectService");
        ArangoInstance ArangoInstance = (ArangoInstance)
                parameters.get("ArangoInstance");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonObject = (JsonObject) jsonParser.parse((String) parameters.get("body"));
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) jsonObject.get("body").toString(), Message.class);

        String s  = "";
        if(message.getBoard_id() == null){
            s+= "board_id is missing";
        }
        if(message.getBoard_object()==null){
            s+= "board_object is missing";
        }
        if(s.isEmpty()) {
            String board = gson.toJson(update_board(message.getBoard_id(), message.getBoard_object(), ArangoInstance, RLiveObjectService));
            jsonObject.add("response",jsonParser.parse(board));
        }else {
            System.out.println(s);
            JSONObject err = new JSONObject();
            err.put("error",s);
            jsonObject.add("response",jsonParser.parse(err.toString()));
        }

        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public BoardDBObject update_board(String board_id, BoardDBObject boardDBObject, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.updateBoard(board_id,boardDBObject);
        BoardLiveObject boardLiveObject = liveObjectService.get(BoardLiveObject.class,board_id);
        if (boardLiveObject!= null){
            boardLiveObject.setName(boardDBObject.getName());
            boardLiveObject.setPosts_id(boardDBObject.getPosts_id());
            boardLiveObject.setUser_id(boardDBObject.getUser_id());
        }
        return arangoInstance.getBoard(board_id);
    }
}