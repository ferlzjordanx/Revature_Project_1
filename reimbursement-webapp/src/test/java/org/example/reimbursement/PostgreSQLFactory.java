package org.example.reimbursement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory class for testing.
 *
 * @author Jorge Ferreira.
 */
public class PostgreSQLFactory {

    /**
     * Open a SQL connection.
     *
     * @return The SQL connection.
     * @throws SQLException Error while connecting.
     */
    public static Connection open() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "postgres";
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(false);
        return conn;
    }
}
