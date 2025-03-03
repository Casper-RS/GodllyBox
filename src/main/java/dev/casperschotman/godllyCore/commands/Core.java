package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.SQLOutput;

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
            sender.sendMessage(getPrefix() + "§cPlease provide a subcommand.");
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
            case "restart":
                if (args.length > 1) {
                    handleRestart(sender, args[1]);
                } else {
                    sender.sendMessage(getPrefix() + "§cPlease specify a time for the restart.");
                }
                break;
            default:
                sender.sendMessage(getPrefix() + "§cUnknown subcommand. Try /godllycore reload.");
                break;
        }
        return true;
    }

    private void handleReload(CommandSender sender) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("godllycore.reload")) {
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

    private void handleRestart(CommandSender sender, String timeArg) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPrefix() + "§cOnly players can execute this command.");
            return;
        }

        if (!player.hasPermission("godllybox.restart")) {
            player.sendMessage(getPrefix() + "§cYou don't have permission to restart the server.");
            return;
        }

        int time;
        try {
            time = Integer.parseInt(timeArg);
        } catch (NumberFormatException e) {
            player.sendMessage(getPrefix() + "§cInvalid time format. Please enter a valid number.");
            return;
        }

        player.sendMessage(getPrefix() + "§aThe server will restart in " + time + " seconds.");

        new BukkitRunnable() {
            int countdown = time;

            @Override
            public void run() {
                if (countdown <= 0) {
                    Bukkit.getServer().shutdown(); // Shutdown the server first
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        try {
                            // Assuming you have a script or command to restart the server
                            Runtime.getRuntime().exec("java -jar " + plugin.getDataFolder().getParentFile().getName() + "/server.jar");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, 20L);
                    cancel();
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getPrefix() + "§erestarting in §6" + countdown + "§e seconds..."));
                    }
                    countdown--;
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20L means 1 second
    }
}