package com.example.eloleaderboard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LeaderboardManager {
    private final JavaPlugin plugin;
    private final DatabaseManager database;
    private final List<ArmorStand> stands = new ArrayList<>();
    private BukkitTask task;
    private Location baseLocation;
    private int interval;
    private int size;

    public LeaderboardManager(JavaPlugin plugin, DatabaseManager database) {
        this.plugin = plugin;
        this.database = database;
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration cfg = plugin.getConfig();
        World world = Bukkit.getWorld(cfg.getString("leaderboard.world", "world"));
        double x = cfg.getDouble("leaderboard.x", 0);
        double y = cfg.getDouble("leaderboard.y", 100);
        double z = cfg.getDouble("leaderboard.z", 0);
        baseLocation = new Location(world, x, y, z);
        interval = cfg.getInt("leaderboard.interval", 200); // ticks
        size = cfg.getInt("leaderboard.size", 5);
    }

    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::update, 20L, interval);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
        }
        clear();
    }

    private void clear() {
        for (ArmorStand stand : stands) {
            if (stand != null && !stand.isDead()) {
                stand.remove();
            }
        }
        stands.clear();
    }

    private void update() {
        clear();
        List<LeaderboardEntry> entries = database.getTopPlayers(size);
        int rank = 1;
        for (LeaderboardEntry entry : entries) {
            Location loc = baseLocation.clone().add(0, -(rank - 1) * 0.4, 0);
            ArmorStand stand = (ArmorStand) baseLocation.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            stand.setSmall(true);
            stand.setMarker(true);
            stand.setGravity(false);
            stand.setCustomNameVisible(true);
            stand.setCustomName(rank + ". " + entry.getPlayer() + " - " + entry.getElo());

            ItemStack head = new ItemStack(org.bukkit.Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(entry.getPlayer().getBytes()), entry.getPlayer());
            meta.setOwnerProfile(profile);
            head.setItemMeta(meta);
            stand.getEquipment().setHelmet(head);

            stands.add(stand);
            rank++;
        }
    }
}
