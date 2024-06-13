package pac;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper {
    private static final String URL = "jdbc:sqlite:game.db";

    public SQLiteHelper() {
        try (Connection conn = connect()) {
            if (conn != null) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS scores ("
                        + "name TEXT NOT NULL, "
                        + "time INTEGER NOT NULL"
                        + ");";
                conn.createStatement().execute(createTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insertScore(String name, long time) {
        String insertSQL = "INSERT INTO scores(name, time) VALUES(?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setLong(2, time);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getTopScores() {
        String querySQL = "SELECT name, time FROM scores ORDER BY time ASC LIMIT 3";
        List<String> topScores = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                topScores.add(rs.getString("name") + ": " + rs.getInt("time"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return topScores;
    }
}