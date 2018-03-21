package PostService;


import java.util.concurrent.*;

public class Main {

    public static void main(String[] args){

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        final ArangoInstance arango = new ArangoInstance("root","pass");

        Callable<String> callable = new Callable() {
            public Object call() throws Exception {
                return arango.getPost("32035");
            }
        };
        
        Future future = executorService.submit(callable);
        try {
            System.out.println("response: "+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
