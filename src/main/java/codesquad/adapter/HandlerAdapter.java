package codesquad.adapter;

import codesquad.handler.Handler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;

public interface HandlerAdapter {
    void handle(HttpRequest request, HttpResponse response, Handler handler);
}
