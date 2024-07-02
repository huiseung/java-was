package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public class IndexViewHandler extends Handler{
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.setBodyFile("/index.html");
    }
}
