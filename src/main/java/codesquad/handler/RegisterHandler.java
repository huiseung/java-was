package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public class RegisterHandler extends Handler{
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.setBodyFile("/registration/index.html");
    }
}
