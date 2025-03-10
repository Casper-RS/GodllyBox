package dev.casperschotman.godllyCore.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BalCheckCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public BalCheckCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("balcheck").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        // Fetch the Skript balance using PlaceholderAPI
        String balanceMessage = PlaceholderAPI.setPlaceholders(player, "%skript_balance%");

        if (balanceMessage == null || balanceMessage.isEmpty() || balanceMessage.equalsIgnoreCase("null")) {
            balanceMessage = "&cBalance not found!";
        }

        // Send fancy chat message
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&8[&6&lBank&8] &fYour balance: " + balanceMessage));

        return true;
    }
}
