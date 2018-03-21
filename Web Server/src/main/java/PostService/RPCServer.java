package PostService;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.*;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "post_queue";
    static ExecutorService executorService = Executors.newFixedThreadPool(15);
    QHandler qHandler;

    public RPCServer() throws IOException{
        qHandler = new QHandler();
        ConnectionFactory factory = new ConnectionFactory();
        final QHandler QHandler = new QHandler();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection      = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

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
                    final byte [] b = body;

                    try {
                        Future future = executorService.submit(new Callable() {
                            public Object call() throws Exception {
                                String message = new String(b,"UTF-8");
                                System.out.println(Thread.currentThread().getName());
                                Gson gson = new GsonBuilder().create();
                                Message msg = gson.fromJson(message, Message.class);

                                return handleMessage(msg);

                            }
                        });

                        System.out.println(future.get());
                        response +=future.get().toString();
                        System.out.println(response);

                    }
                    catch (RuntimeException e){
                        System.out.println(" [.] " + e.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } finally {
                        channel.basicPublish( "", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                        channel.basicAck(envelope.getDeliveryTag(), false);
                        // RabbitMq consumer worker thread notifies the RPC server owner thread
                        synchronized(this) {
                            this.notify();
                        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {}
        }
    }

    public  Object handleMessage(Message msg){

        String method = msg.getMethod();
        Object returned = null;


        switch (method){
            case "insert_post":
                PostDBObject postDBObject = msg.getPost_object();
                returned = qHandler.insert_post(postDBObject);
                break;
            case "get_post":
                PostLiveObject post= qHandler.getPost(msg.getPost_id());
                returned = post;
                break;

            case "delete_post":
                qHandler.deletePost(msg.getPost_id());
                returned = "deleted";
                break;

            case "get_category":
                returned = qHandler.getCategory(msg.getCategory_id());
                break;

            case "update_post":
                returned = qHandler.update_post(msg.getPost_id(),msg.getPost_object());
                break;
            case "insert_category":
                CategoryDBObject categoryDBObject = msg.getCategory_object();
                returned = qHandler.insert_category(categoryDBObject);
                break;

            case "delete_category":
                qHandler.deleteCategory(msg.getCategory_id());
                returned = "deleted";
                break;

            case "update_category":
                returned =  qHandler.updateCategoty(msg.getCategory_id(),msg.getCategory_object());
                break;

        }
        return returned;
    }


    public static void main(String[] argv) throws IOException {
        RPCServer rpcServer = new RPCServer();
    }

}
