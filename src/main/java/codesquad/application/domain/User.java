package codesquad.application.domain;

public class User {
    private final String username;
    private final String password;
    private final String nickname;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + username + '\'' +
                ", password=" + password + '\'' +
                ", nickname=" + nickname + '\'' +
                '}';
    }
}
