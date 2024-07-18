package codesquad.webserver.database.csv;

import codesquad.application.domain.User;
import codesquad.webserver.database.DatabaseConnectionManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CscDriverTest {
    private Logger log = LoggerFactory.getLogger(CsvJdbcDriver.class);

    @Test
    public void test(){
        try{
            Class.forName("codesquad.webserver.database.csv.CsvJdbcDriver");

            try(Connection con = CsvConnectionManager.getConnection()){
                try(Statement stmt = con.createStatement()){
                    String query;
                    query = "DROP TABLE IF EXISTS USERS";
                    stmt.execute(query);
                    log.info("user table has benn dropped");
                    query = "CREATE TABLE IF NOT EXISTS USERS (" +
                            "ID," +
                            "USERNAME," +
                            "PASSWORD," +
                            "NICKNAME" +
                            ")";
                    stmt.execute(query);
                    log.info("Users table created or already exists.");
                    insert();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void insert(){
        User insertUser = new User(UUID.randomUUID().toString(), "a", "b", "c");
        String sql = "INSERT INTO USERS (ID, USERNAME, PASSWORD, NICKNAME) VALUES (?, ?, ?, ?)";
        try (Connection con = CsvConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, insertUser.getId());
            pstmt.setString(2, insertUser.getUsername());
            pstmt.setString(3, insertUser.getPassword());
            pstmt.setString(4, insertUser.getNickname());
            int affectedRows = pstmt.executeUpdate();
            assertEquals(1, affectedRows);
            log.info("Inserted user: " + insertUser.toString() + ", Affected rows: " + affectedRows);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }
}
