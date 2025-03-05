package dev.casperschotman.godllyCore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import dev.casperschotman.godllyCore.GodllyCore;

import java.util.HashSet;
import java.util.Set;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class VanishCommand implements CommandExecutor {

    private static final Set<Player> vanishedPlayers = new HashSet<>();
    private final GodllyCore plugin;

    public VanishCommand(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (!player.hasPermission("godllycore.vanish")) {
            player.sendMessage(getPrefix() + "§cYou don't have permission to use this command.");
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            return true;
        }

        if (vanishedPlayers.contains(player)) {
            // Unvanish the player
            vanishedPlayers.remove(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
            }
            player.sendMessage(getPrefix() + "§aYou are now visible to other players.");
        } else {
            // Vanish the player
            vanishedPlayers.add(player);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("godllycore.vanish.see")) {
                    onlinePlayer.hidePlayer(plugin, player);
                }
            }
            player.sendMessage(getPrefix() + "§aYou are now vanished.");
            showVanishReminder(player); // Start action bar reminder
        }

        return true;
    }

    public static boolean isVanished(Player player) {
        return vanishedPlayers.contains(player);
    }

    private void showVanishReminder(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!vanishedPlayers.contains(player) || !player.isOnline()) {
                    this.cancel();
                    return;
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6You are currently vanished"));
            }
        }.runTaskTimer(plugin, 0L, 40L); // Updates every 2 seconds
    }
}
