package PostService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.HashMap;


public class insertPostToBoard extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonObject = (JsonObject) jsonParser.parse((String) parameters.get("body"));
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) jsonObject.get("body").toString(), Message.class);


        String board = gson.toJson(insertPostToBoard(message.getPost_id(),message.getBoard_id()));
        jsonObject.add("response",jsonParser.parse(board));
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public BoardDBObject insertPostToBoard(String post_id,String board_id){
        arangoInstance.insertPostToBoard(board_id,post_id);
        BoardDBObject boardDBObject = arangoInstance.getBoard(board_id);
        BoardLiveObject boardLiveObject = liveObjectService.get(BoardLiveObject.class,board_id);
        if(boardLiveObject != null){
            boardLiveObject.setName(boardDBObject.getName());
            boardLiveObject.setPosts_id(boardDBObject.getPosts_id());
            boardLiveObject.setUser_id(boardDBObject.getUser_id());
        }
        return boardDBObject;

    }
}