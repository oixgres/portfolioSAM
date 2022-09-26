package logingestor;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBManager{
    private final String endpoint = System.getenv("DB_ENDPOINT");
    private final String user = System.getenv("DB_USER");
    private final String password = System.getenv("DB_PASSWORD");

    public static final String query= "INSERT INTO visit_logs" +
    "(time, ip, country, state, city, zip, lat, lon, status, reason, page)" +
    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public void insertRecord(VisitLog log) throws SQLException {
        boolean status = false;

        try (Connection conn = DriverManager.getConnection(endpoint, user, password);
            PreparedStatement preparedStatement = conn.prepareStatement(query)){
            if (log.status == "success")
                status = true;
    
            preparedStatement.setString(1, log.time);
            preparedStatement.setString(2, log.ip);
            preparedStatement.setString(3, log.country);
            preparedStatement.setString(4, log.state);
            preparedStatement.setString(5, log.city);
            preparedStatement.setString(6, log.zip);
            preparedStatement.setInt(7, log.lat);
            preparedStatement.setInt(8, log.lon);
            preparedStatement.setBoolean(9, status);
            preparedStatement.setString(10, log.reason);
            preparedStatement.setString(11, log.page);
    
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();

                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}