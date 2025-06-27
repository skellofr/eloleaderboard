package com.example.eloleaderboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EloLeaderboardPlugin extends JavaPlugin {
    private DatabaseManager databaseManager;
    private LeaderboardManager leaderboardManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        databaseManager = new DatabaseManager(getConfig());
        leaderboardManager = new LeaderboardManager(this, databaseManager);
        leaderboardManager.start();
        getLogger().info("EloLeaderboard enabled");
    }

    @Override
    public void onDisable() {
        if (leaderboardManager != null) {
            leaderboardManager.stop();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("EloLeaderboard disabled");
    }
}
