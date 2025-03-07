package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final GodllyCore plugin;

    public PlayerRespawnListener(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location respawnLocation = getDeathSpawnLocation();

        if (respawnLocation != null) {
            event.setRespawnLocation(respawnLocation);
            player.sendMessage(ChatColor.GREEN + "You have respawned at the server spawn!");
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            player.sendMessage(ChatColor.RED + "No custom spawn location set. Respawning at default spawn.");
        }
    }

    private Location getDeathSpawnLocation() {
        plugin.reloadConfig(); // ✅ Ensure we use the latest config
        FileConfiguration config = plugin.getConfig();

        if (config.contains("death.spawn")) {
            String worldName = config.getString("death.spawn.world", "new1");
            double x = config.getDouble("death.spawn.x", 4.5);
            double y = config.getDouble("death.spawn.y", -59);
            double z = config.getDouble("death.spawn.z", 3.5);
            float yaw = (float) config.getDouble("death.spawn.yaw", 90);
            float pitch = (float) config.getDouble("death.spawn.pitch", 0);

            return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        }
        return null;
    }
}
