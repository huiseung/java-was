package codesquad.app.handler;

import codesquad.handler.OperationHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public class IndexViewHandler extends OperationHandler {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.setBodyFile("/index.html");
    }
}
