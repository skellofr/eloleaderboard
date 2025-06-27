package com.example.eloleaderboard;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final String url;
    private final String user;
    private final String password;
    private Connection connection;

    public DatabaseManager(FileConfiguration config) {
        String host = config.getString("database.host", "localhost");
        int port = config.getInt("database.port", 3306);
        String db = config.getString("database.name", "minecraft");
        this.user = config.getString("database.user", "root");
        this.password = config.getString("database.password", "");
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useSSL=false";
        open();
    }

    private void open() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<LeaderboardEntry> getTopPlayers(int limit) {
        List<LeaderboardEntry> list = new ArrayList<>();
        if (connection == null) {
            return list;
        }
        String sql = "SELECT player, elo FROM elo ORDER BY elo DESC LIMIT ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String player = rs.getString("player");
                    int elo = rs.getInt("elo");
                    list.add(new LeaderboardEntry(player, elo));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
