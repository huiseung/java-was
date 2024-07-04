package codesquad.app.handler;

import codesquad.annotation.RequestHandler;
import codesquad.annotation.RequestMapping;
import codesquad.http.HttpMethod;
import codesquad.model.User;

@RequestHandler
public class RegisterApiHandlerV2 {
    @RequestMapping(method = HttpMethod.GET, path = "/create")
    public String create(){
        User user = new User("id", "password", "nickname");
        return user.toString();
    }
}
