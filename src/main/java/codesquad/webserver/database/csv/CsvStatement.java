package codesquad.webserver.database.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CsvStatement implements Statement {
    private CsvConnection connection;
    private List<String[]> data;
    private String csvDirectory;
    private final Logger log = LoggerFactory.getLogger(CsvStatement.class);

    public CsvStatement(CsvConnection connection) {
        this.connection = connection;
        this.csvDirectory = connection.getCsvFilePath();
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        if(!sql.toLowerCase().startsWith("select")){
            throw new SQLException("");
        }
        loadCsvData();
        return new CsvResultSet(data);
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        loadCsvData();
        String lowerSql = sql.toLowerCase();
        if(lowerSql.startsWith("insert")){
            return handleInsert(sql);
        }else if(lowerSql.startsWith("update")){
            return handleUpdate(sql);
        }else if(lowerSql.startsWith("delete")){
            return handleDelete(sql);
        }else{
            throw new SQLException("Unsupported SQL Operation");
        }
    }

    private void loadCsvData() throws SQLException{
        data = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(connection.getCsvFilePath()))){
            String line;
            while((line = br.readLine()) != null){
                data.add(line.split(","));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCsvData() throws SQLException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(connection.getCsvFilePath()))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new SQLException("Error writing CSV file", e);
        }
    }

    private int handleInsert(String sql) throws SQLException {
        // INSERT INTO table VALUES (val1, val2, ...)
        String[] parts = sql.split("VALUES");
        if (parts.length != 2) throw new SQLException("Invalid INSERT statement");

        String values = parts[1].trim().replaceAll("[()]", "");
        String[] newRow = values.split(",");
        for (int i = 0; i < newRow.length; i++) {
            newRow[i] = newRow[i].trim();
        }
        data.add(newRow);
        saveCsvData();
        log.info("insert: save csv");
        return 1;
    }

    private int handleUpdate(String sql) throws SQLException {
        // UPDATE table SET col1 = val1 WHERE col2 = val2
        String[] parts = sql.split("SET|WHERE");
        if (parts.length != 3) throw new SQLException("Invalid UPDATE statement");

        String[] setParts = parts[1].trim().split("=");
        String updateColumn = setParts[0].trim();
        String updateValue = setParts[1].trim();

        String[] whereParts = parts[2].trim().split("=");
        String whereColumn = whereParts[0].trim();
        String whereValue = whereParts[1].trim();

        int updateCount = 0;
        int updateColumnIndex = Arrays.asList(data.get(0)).indexOf(updateColumn);
        int whereColumnIndex = Arrays.asList(data.get(0)).indexOf(whereColumn);

        for (int i = 1; i < data.size(); i++) {
            if (data.get(i)[whereColumnIndex].equals(whereValue)) {
                data.get(i)[updateColumnIndex] = updateValue;
                updateCount++;
            }
        }

        saveCsvData();
        return updateCount;
    }

    private int handleDelete(String sql) throws SQLException {
        // DELETE FROM table WHERE col = val
        String[] parts = sql.split("WHERE");
        if (parts.length != 2) throw new SQLException("Invalid DELETE statement");

        String[] whereParts = parts[1].trim().split("=");
        String whereColumn = whereParts[0].trim();
        String whereValue = whereParts[1].trim();

        int deleteCount = 0;
        int whereColumnIndex = Arrays.asList(data.get(0)).indexOf(whereColumn);

        Iterator<String[]> iterator = data.iterator();
        iterator.next();
        while (iterator.hasNext()) {
            String[] row = iterator.next();
            if (row[whereColumnIndex].equals(whereValue)) {
                iterator.remove();
                deleteCount++;
            }
        }
        saveCsvData();
        return deleteCount;
    }


    @Override
    public boolean execute(String sql) throws SQLException {
        String lowerSql = sql.toLowerCase().trim();
        if (lowerSql.startsWith("create table")) {
            handleCreateTable(sql);
            return false; // DDL 명령은 false를 반환
        } else if (lowerSql.startsWith("drop table")) {
            handleDropTable(sql);
            return false; // DDL 명령은 false를 반환
        }
        return false;
    }

    private void handleCreateTable(String sql) throws SQLException {
        // CREATE TABLE IF NOT EXISTS table_name (column1, column2, ...)
        String[] parts = sql.split("\\s+", 7);
        if (parts.length < 7 || !parts[2].equalsIgnoreCase("IF") || !parts[3].equalsIgnoreCase("NOT") || !parts[4].equalsIgnoreCase("EXISTS")) {
            throw new SQLException("Invalid CREATE TABLE statement");
        }

        String tableName = parts[5];
        String columnDefinition = parts[6].replaceAll("[()]", "").trim();
        String[] columns = columnDefinition.split(",");
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columns[i].trim().split("\\s+")[0]; // 컬럼 이름만 추출
        }

        File file = new File(csvDirectory, tableName + ".csv");
        if (file.exists()) {
            log.debug("Table already exists: " + tableName);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(String.join(",", columns));
            writer.newLine();
            log.debug("Table created: " + tableName);
        } catch (IOException e) {
            throw new SQLException("Error creating CSV file", e);
        }
    }
    private void handleDropTable(String sql) throws SQLException {
        // DROP TABLE IF EXISTS table_name
        String[] parts = sql.split("\\s+");
        if (parts.length != 5 || !parts[2].equalsIgnoreCase("IF") || !parts[3].equalsIgnoreCase("EXISTS")) {
            throw new SQLException("Invalid DROP TABLE statement");
        }

        String tableName = parts[4];
        File file = new File(csvDirectory, tableName + ".csv");

        if (!file.exists()) {
            log.debug("Table does not exist: " + tableName);
            return;
        }

        if (file.delete()) {
            log.debug("Table dropped: " + tableName);
        } else {
            throw new SQLException("Error dropping CSV file: " + tableName);
        }
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {

    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) throws SQLException {

    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }

    @Override
    public void addBatch(String sql) throws SQLException {

    }

    @Override
    public void clearBatch() throws SQLException {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public Connection getConnection() throws SQLException {
        return null;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {

    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
