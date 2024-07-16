package codesquad.application.datahandler;

import codesquad.application.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDataHandler {
    void insert(User user);
    List<User> getAll();
    boolean isExist(String username);
    Optional<User> getByUsername(String username);
}
