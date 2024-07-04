package codesquad.webserver;

import codesquad.handler.Handler;
import codesquad.handler.HandlerSelector;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private final Socket connect;
    private final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(Socket connect) {
        this.connect = connect;
    }

    public void run() {
        log.debug("Client Connected IP: {}, Port: {}", connect.getInetAddress(), connect.getPort());
        try (InputStream inputStream = connect.getInputStream();
             OutputStream outputStream = connect.getOutputStream();
        ) {
            HttpRequest request = new HttpRequest(inputStream);
            log.debug("request: {}", request);
            HttpResponse response = new HttpResponse(outputStream);
            //
            Handler handler = HandlerSelector.getHandler(request.getPath());
            if (handler == null) {
                response.setNotFound();
            } else {
                handler.printClassName();
                handler.handle(request, response);
            }
            connect.close();
            log.info("response: {}", response);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
