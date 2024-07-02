package codesquad.handler;

import codesquad.http.HttpRequest;
import codesquad.http.HttpResponse;
import codesquad.model.User;

public class RegisterApiHandler extends Handler{

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        String queryString = request.getQueryString();
        String[] query = queryString.split("&");
        String userId = query[0].split("=")[1];
        String nickname = query[1].split("=")[1];
        String password = query[2].split("=")[1];
        User user = new User(userId, password, nickname);
        log.debug("user: "+user);
    }
}
