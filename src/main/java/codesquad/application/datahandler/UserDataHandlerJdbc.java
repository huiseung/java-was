package codesquad.application.datahandler;

import codesquad.application.domain.User;
import codesquad.webserver.annotation.DataHandler;

import java.util.List;
import java.util.Optional;

@DataHandler("UserDataHandlerJdbc")
public class UserDataHandlerJdbc implements UserDataHandler{
    @Override
    public void insert(User user) {

    }

    @Override
    public List<User> getAll() {
        return List.of();
    }

    @Override
    public boolean isExist(String username) {
        return false;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.empty();
    }
}
