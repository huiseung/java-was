package codesquad.application.datahandler;

import codesquad.webserver.annotation.DataHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.http.HttpMethod;

@DataHandler("UserDataHandlerJdbc")
public class UserDataHandlerJdbc implements UserDataHandler{
    @RequestMapping(method = HttpMethod.GET, path = "/hello")
    public String hello(){
        return "hello";
    }
}
