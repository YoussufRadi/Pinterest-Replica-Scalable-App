import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class UserConsumer {
   static Channel channel;
   static Chat chat;
    public UserConsumer(String username, Channel channel) throws IOException, TimeoutException {
        this.channel=channel;
        channel.exchangeDeclare(username, BuiltinExchangeType.FANOUT);
        String queueName=channel.queueDeclare().getQueue();
        channel.queueBind(queueName,username,"");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message= new String(body,"UTF-8");
                String[] msg= message.split(":");
                System.out.println(message+" 3nd el consumer");
                System.out.println(Chat.servername);
                Chat.sendusermessage(msg[0],msg[1]+":"+msg[2]);

                //   channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));

            }
        };

        channel.basicConsume(queueName, true, consumer);

    }
    public static void close(String username) throws IOException {
        channel.exchangeDelete(username);
    }
}


