package msa.messagequeue;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;
import msa.pojo.User;
import msa.postgresql.DatabaseController;

import java.io.IOException;
import java.util.concurrent.*;

public class ReceiveLogs {
  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(message+"messsage");

        Gson gson = new GsonBuilder().create();
        User user = gson.fromJson(message, User.class);
        System.out.println(" [x] Received '" + user.getFirstName()+ "'");
        DatabaseController dc = new DatabaseController();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Future future = executorService.submit(new Callable(){
          public Object call() throws Exception {
            return dc.addUser(user);
          }
        });

        try {
          System.out.println("future.get() = " + future.get());
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        }

        dc.addUser(user);




      }
    };
    channel.basicConsume(queueName, true, consumer);
  }
}

