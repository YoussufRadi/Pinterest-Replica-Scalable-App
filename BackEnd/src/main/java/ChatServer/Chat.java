package ChatServer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import static j2html.TagCreator.*;
import static spark.Spark.*;

public class Chat {
    static Channel channel;
    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    static Connection connection;
    static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
    static Map<String, Session> usersessionmap = new ConcurrentHashMap<>();
    static String servername;

    static int nextUserNumber = 1; //Assign to username for next connecting user

    public static void main(String[] args) throws IOException, TimeoutException {
       // port(8000);
        ConnectionFactory factory= new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        connection = factory.newConnection();
        channel=connection.createChannel();
        servername="server"+System.identityHashCode(connection);

        staticFiles.location("/public"); //index.html is served at localhost:4567 (default port)
        staticFiles.expireTime(600);
        webSocket("/chat", ChatWebSocketHandler.class);
        init();
    }

    //Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
        userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void sendusermessage(String sender, String message) {
        try {
            String []key=message.split(":");
            System.out.println(message+"fel chat");
            Session reciever= Chat.usersessionmap.get(key[0]);

            reciever.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(sender, key[1]))
                    .put("userlist", userUsernameMap.get(reciever))
            ));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private static String createHtmlMessageFromSender(String sender, String message) {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
                p(message)
        ).render();
    }

}