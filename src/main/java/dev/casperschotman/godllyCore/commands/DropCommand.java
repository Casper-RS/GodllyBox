package dev.casperschotman.godllyCore.commands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class DropCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> playerCooldown = new HashMap<>();
    private final HashMap<UUID, Boolean> cooldownBypass = new HashMap<>();

    // Constructor to pass the plugin instance
    public DropCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            if (!player.hasPermission("godllycore.drop.bypass")) {
                player.sendMessage(getPrefix() + "§cYou don't have permission to toggle drop cooldown.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                return true;
            }

            boolean isBypassed = cooldownBypass.getOrDefault(player.getUniqueId(), false);
            cooldownBypass.put(player.getUniqueId(), !isBypassed);

            player.sendMessage(getPrefix() + (isBypassed ? "§cDrop cooldown is now enabled." : "§aDrop cooldown is now disabled."));
            return true;
        }

        if (hasBypass(player.getUniqueId())) {
            player.sendMessage(getPrefix() + "§aYou have bypassed the drop cooldown!");
            return true;
        }

        // Get cooldown time from the config file
        int cooldownSeconds = plugin.getConfig().getInt("drop-cooldown", 30); // Default to 30 seconds if not set in config
        long cooldownMillis = cooldownSeconds * 1000L;  // Convert to milliseconds

        // Set the cooldown for the player
        long currentTime = System.currentTimeMillis();
        playerCooldown.put(player.getUniqueId(), currentTime + cooldownMillis);  // Set cooldown

        // Send message to player that they can drop items for the cooldown period
        player.sendMessage(" ");
        player.sendMessage(getPrefix() + "§aYou can now drop items for §e" + cooldownSeconds + "§a seconds!");

        return true;
    }

    public long getPlayerCooldown(UUID playerUUID) {
        return playerCooldown.getOrDefault(playerUUID, 0L);
    }

    public boolean hasBypass(UUID playerUUID) {
        return cooldownBypass.getOrDefault(playerUUID, false);
    }
}
