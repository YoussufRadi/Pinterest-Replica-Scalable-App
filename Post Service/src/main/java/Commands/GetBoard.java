package Commands;


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

import java.io.IOException;
import java.util.HashMap;


public class GetBoard extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonObject = (JsonObject)jsonParser.parse((String) parameters.get("body"));
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) jsonObject.get("body").toString(),Message.class);
        String board = gson.toJson(getBoard(message.getBoard_id()));
        if(board != null) {
            jsonObject.add("response", jsonParser.parse(board));
        }else {
            jsonObject.add("response",  new JsonObject());
        }

        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public BoardLiveObject getBoard(String board_id ){
        BoardLiveObject boardLiveObject = liveObjectService.get(BoardLiveObject.class,board_id);
        if(boardLiveObject==null){
            BoardDBObject boardDBObject= arangoInstance.getBoard(board_id);
            if(boardDBObject!=null) {
                String message = new Gson().toJson(boardDBObject);
                Gson gson = new GsonBuilder().create();
                boardLiveObject = gson.fromJson(message, BoardLiveObject.class);
                boardLiveObject = liveObjectService.merge(boardLiveObject);
                //return categoryLiveObject;
            }
        }
        return boardLiveObject;
    }

}
