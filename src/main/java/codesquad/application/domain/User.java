package codesquad.application.domain;



public class User {
    private final String id;
    private final String username;
    private final String password;
    private final String nickname;

    public User(String id, String username, String password, String nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public User(String username, String password, String nickname) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }
}
