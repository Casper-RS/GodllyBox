package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class Core implements CommandExecutor {

    private final GodllyCore plugin;

    public Core(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // If no subcommand is provided
            sender.sendMessage(getPrefix() + "§cPlease provide a subcommand (e.g., /godllycore reload or /godllycore info).");
            return true;
        }

        // Handle subcommands
        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "info":
                handleInfo(sender);
                break;
            default:
                sender.sendMessage(getPrefix() + "§cUnknown subcommand. Try /godllycore reload or /godllycore info.");
                break;
        }
        return true;
    }

    private void handleReload(CommandSender sender) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("godllybox.reload")) {
                player.sendMessage(getPrefix() + "§cYou don't have permission to reload the configuration.");
                return;
            }
        }

        // Reload the configuration
        plugin.reloadConfig();
        sender.sendMessage(getPrefix() + "§aConfiguration reloaded successfully!");
    }

    private void handleInfo(CommandSender sender) {
        String version = plugin.getDescription().getVersion(); // Get the plugin version from plugin.yml
        String author = plugin.getDescription().getAuthors().getFirst(); // Get the author from plugin.yml

        sender.sendMessage(" ");
        sender.sendMessage("§8--------[§3GodllyCore Info§8]--------");
        sender.sendMessage("§3Version: §e" + version);
        sender.sendMessage("§3Developer: §e" + author);
        sender.sendMessage(" ");
        sender.sendMessage("§aA Custom plugin for GodllyBox!");
        sender.sendMessage("§8-------------------------------------");
    }
}
