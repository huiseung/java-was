package codesquad.app.handler;

import codesquad.handler.OperationHandler;
import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.model.User;

public class RegisterApiHandler extends OperationHandler {

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        String queryString = request.getQueryString();
        String[] query = queryString.split("&");
        String userId = query[0].split("=")[1];
        String nickname = query[1].split("=")[1];
        String password = query[2].split("=")[1];
        User user = new User(userId, password, nickname);
        log.debug("user: "+user);
        response.setRedirection("/index");
    }
}
