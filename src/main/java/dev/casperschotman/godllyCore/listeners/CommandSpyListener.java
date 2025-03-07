package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class CommandSpyListener implements Listener {
    private final GodllyCore plugin;
    private final Set<UUID> spyingPlayers = new HashSet<>();

    public CommandSpyListener(GodllyCore plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String command = event.getMessage(); // Full command (e.g., "/spawn")

        for (UUID uuid : spyingPlayers) {
            if (uuid.equals(sender.getUniqueId())) {
                continue; // Skip logging the player's own command
            }

            Player spy = Bukkit.getPlayer(uuid);
            if (spy != null && spy.isOnline()) {
                spy.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "CSpy" + ChatColor.DARK_GRAY + "] " +
                        ChatColor.DARK_RED + sender.getName() + ChatColor.RED + ": " + ChatColor.GRAY + command);
            }
        }
    }

    public void toggleCommandSpy(Player player) {
        UUID playerId = player.getUniqueId();
        if (spyingPlayers.contains(playerId)) {
            spyingPlayers.remove(playerId);
            player.sendMessage(getPrefix() + "§cCommand Spy disabled.");
        } else {
            spyingPlayers.add(playerId);
            player.sendMessage(getPrefix() + "§aCommand Spy enabled.");
        }
    }
}
