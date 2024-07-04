package codesquad.handler;

import codesquad.http.HttpMethod;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OperationHandler implements Handler {
    protected final Logger log = LoggerFactory.getLogger(Handler.class);

    public void handle(HttpRequest request, HttpResponse response){
        HttpMethod method = request.getMethod();
        if(method.isPost()){
            doPost(request, response);
        }else if(method.isGet()){
            doGet(request, response);
        }
    }

    public void printClassName(){
        log.debug("name: "+getClass().getName());
    }

    protected void doGet(HttpRequest request, HttpResponse response){}

    protected void doPost(HttpRequest request, HttpResponse response){}
}
