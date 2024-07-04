package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.util.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceHandler implements Handler{
    protected final Logger log = LoggerFactory.getLogger(Handler.class);

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        if(!FileReader.hasFile(request.getPath())){
            response.setNotFound();
            return;
        }
        response.setBodyFile(request.getPath());
    }

    @Override
    public void printClassName() {
        log.debug("name: "+getClass().getName());
    }
}
