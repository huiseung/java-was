package codesquad.handler;


import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Handler {

    void handle(HttpRequest request, HttpResponse response);
    void printClassName();
}
