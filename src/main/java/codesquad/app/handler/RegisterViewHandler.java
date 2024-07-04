package codesquad.app.handler;

import codesquad.handler.OperationHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public class RegisterViewHandler extends OperationHandler {
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.setBodyFile("/registration/index.html");
    }
}
