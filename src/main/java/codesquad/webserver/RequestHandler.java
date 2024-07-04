package codesquad.webserver;

import codesquad.adapter.HandlerAdapter;
import codesquad.adapter.OperationHandlerAdapter;
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
            // handler 를 찾는다
            Handler handler = HandlerSelector.getHandler(request);
            // handler 를 처리 할 수 있는 adapter 를 찾는다
            // (ResourceHttpRequestHandler, HttpRequestHandlerAdapter, /path가 없으면 adapter가 handler에게 맞겼는데 오류 발생)
            HandlerAdapter handlerAdapter = new OperationHandlerAdapter();
            // adapter 에게 handle 시킨다
            handlerAdapter.handle(request, response, handler);

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
