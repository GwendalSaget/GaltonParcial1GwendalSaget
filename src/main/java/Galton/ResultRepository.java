package Galton;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class ResultRepository {

    public ResultRepository() {
        createTableIfNotExists();
    }

    public void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "result_value INTEGER NOT NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveResult(int result) {
        String insertSQL = "INSERT INTO results (result_value) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             var preparedStatement = conn.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, result);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
