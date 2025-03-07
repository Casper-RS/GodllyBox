package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class TeleportListener implements Listener {
    private final GodllyCore plugin;
    private final Map<UUID, Location> lastTeleportLocation = new HashMap<>();
    private final Map<UUID, Location> lastDeathLocation = new HashMap<>();

    public TeleportListener(GodllyCore plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        lastTeleportLocation.put(player.getUniqueId(), event.getFrom());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        lastDeathLocation.put(player.getUniqueId(), player.getLocation());
    }

    public boolean teleportBack(Player player) {
        UUID playerId = player.getUniqueId();

        if (lastTeleportLocation.containsKey(playerId)) {
            player.teleport(lastTeleportLocation.get(playerId));
            player.sendMessage(getPrefix() + ChatColor.GREEN + "Teleported to your last location before teleporting.");
            lastTeleportLocation.remove(playerId); // Remove after use
            return true;
        } else if (lastDeathLocation.containsKey(playerId)) {
            player.teleport(lastDeathLocation.get(playerId));
            player.sendMessage(getPrefix() + ChatColor.GREEN + "Teleported to your last death location.");
            return true;
        } else {
            player.sendMessage(getPrefix() + ChatColor.RED + "No previous location recorded.");
            return false;
        }
    }
}
