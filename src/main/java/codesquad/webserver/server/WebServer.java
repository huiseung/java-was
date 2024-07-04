package codesquad.webserver.server;

import codesquad.webserver.handler.FrontHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    public static final int PORT = 8080;
    private final Logger log = LoggerFactory.getLogger(WebServer.class);
    private ExecutorService executorService;
    private ServerSocket serverSocket;


    public WebServer() {
        try{
            int N_THREADS = 10;
            this.executorService = Executors.newFixedThreadPool(N_THREADS);
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void run(){
        while(true){
            try{
                log.info("Listening for connection on port 8080 ....");
                executorService.submit(new FrontHandler(serverSocket.accept()));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
