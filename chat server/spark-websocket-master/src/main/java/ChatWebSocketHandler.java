import com.rabbitmq.client.*;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;
    private UserConsumer consumer;
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        double s=Math.random()*100;
        int num=(int)s;
        String username = "User" + num;        Channel channel= Chat.connection.createChannel();
        consumer=new UserConsumer(username,channel);
        Chat.userUsernameMap.put(user, username);
        Chat.usersessionmap.put(username, user);

        Chat.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) throws IOException {
        String username = Chat.userUsernameMap.get(user);
        consumer.channel.exchangeDelete(username);
      //  consumer.channel.abort();

        Chat.userUsernameMap.remove(user);
        Chat.usersessionmap.remove(username);

        Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {
        String []key=message.split(":");
        Session reciever= Chat.usersessionmap.get(key[0]);
        //  System.out.println(reciever);
        if(reciever==null){

            consumer.channel.exchangeDeclare(key[0], BuiltinExchangeType.FANOUT);
            consumer.channel.basicPublish(key[0],"",null,
                    (Chat.userUsernameMap.get(user)+":"+(message)).getBytes());
            System.out.println(message+" fel handler");
            // channel.close();
            //  connection.close();
        }
        else {
            Chat.sendusermessage(sender = Chat.userUsernameMap.get(user), msg = message);
        }    }

}
