package codesquad.application.handler;


import codesquad.application.database.UserDatabase;
import codesquad.application.domain.User;
import codesquad.application.session.CookieExtractor;
import codesquad.application.session.SessionManager;
import codesquad.webserver.annotation.ApiHandler;
import codesquad.webserver.annotation.RequestMapping;
import codesquad.webserver.http.HttpMethod;
import codesquad.webserver.http.HttpRequest;
import codesquad.webserver.http.HttpResponse;
import codesquad.webserver.util.JsonStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


@ApiHandler
public class UserHandler {
    private final Logger log = LoggerFactory.getLogger(RegistrationHandler.class);

    @RequestMapping(method = HttpMethod.GET, path="/me")
    public HttpResponse me(HttpRequest request){
        String sid = CookieExtractor.getSid(request);
        if(sid == null){
            return HttpResponse.ok("NO_USER");
        }
        User user = SessionManager.getInstance().getUser(sid);
        return HttpResponse.ok(user.getNickname());
    }

    @RequestMapping(method = HttpMethod.GET, path="/api/users/list")
    public HttpResponse getUserList(HttpRequest request){
        Collection<User> users = UserDatabase.getInstance().getUsers();
        String responseBody = JsonStringConverter.collectionToJsonString(users);
        log.debug("users/list: " + responseBody);
        return HttpResponse.ok(responseBody);
    }
}
