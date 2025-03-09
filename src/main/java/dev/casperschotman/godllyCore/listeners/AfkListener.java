package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.*;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class AfkListener implements Listener {
    private final GodllyCore plugin;
    private final Map<UUID, Long> afkTimers = new HashMap<>();
    private final Set<UUID> afkPlayers = new HashSet<>();

    public AfkListener(GodllyCore plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        startAfkChecker();
    }

    // Update AFK timer when player moves
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        updateAfkTimer(event.getPlayer());
    }

    // Update AFK timer when player interacts
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        updateAfkTimer(event.getPlayer());
    }

    // Update AFK timer when player chats
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        updateAfkTimer(event.getPlayer());
    }

    // Update AFK timer when player breaks a block
    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event) {
        updateAfkTimer(event.getPlayer());
    }

    // Update AFK timer when player joins
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updateAfkTimer(event.getPlayer());
    }

    private void updateAfkTimer(Player player) {
        afkTimers.put(player.getUniqueId(), System.currentTimeMillis());
        removeAfkIfNeeded(player);
    }

    private void removeAfkIfNeeded(Player player) {
        if (afkPlayers.contains(player.getUniqueId())) {
            afkPlayers.remove(player.getUniqueId());
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is no longer AFK.");
        }
    }

    private void startAfkChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long afkTime = plugin.getConfig().getLong("afk-time", 60) * 1000; // Default: 60 seconds

                for (UUID uuid : new HashSet<>(afkTimers.keySet())) { // Prevents ConcurrentModificationException
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null || !player.isOnline()) continue;

                    long lastActive = afkTimers.getOrDefault(uuid, System.currentTimeMillis());
                    if (System.currentTimeMillis() - lastActive > afkTime && !afkPlayers.contains(uuid)) {
                        toggleAfk(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 20, 20); // Check every second
    }
    public boolean isAfk(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    public void toggleAfk(Player player) {
        if (player == null || !player.isOnline()) return;

        UUID uuid = player.getUniqueId();

        if (afkPlayers.contains(uuid)) {
            afkPlayers.remove(uuid);
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is no longer AFK.");
        } else {
            afkPlayers.add(uuid);
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is now AFK.");
        }
    }

}
