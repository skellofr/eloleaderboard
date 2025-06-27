package com.example.eloleaderboard;

public class LeaderboardEntry {
    private final String player;
    private final int elo;

    public LeaderboardEntry(String player, int elo) {
        this.player = player;
        this.elo = elo;
    }

    public String getPlayer() {
        return player;
    }

    public int getElo() {
        return elo;
    }
}
