package Galton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:src/galton_results.db"; // Chemin vers la base de données SQLite

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void clearDatabase() {
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("DELETE FROM results;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}