package codesquad.application.apihandler;

import codesquad.application.datahandler.UserDataHandler;
import codesquad.application.datahandler.UserDatabase;
import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.annotation.Specify;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;

@ApiHandler
public class HelloHandler {
    private final UserDataHandler userDataHandler;

    public HelloHandler(@Specify("UserDataHandlerJdbc") UserDataHandler userDataHandler) {
        this.userDataHandler = userDataHandler;
    }

    @RequestMapping(method = HttpMethod.GET, path = "/hello")
    public HttpResponse hello(HttpRequest request){
        return HttpResponse.ok(userDataHandler.hello());
    }
}
