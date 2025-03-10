package dev.casperschotman.godllyCore.commands;
import dev.casperschotman.godllyCore.GodllyCore;
import dev.casperschotman.godllyCore.listeners.*;
import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import dev.casperschotman.godllyCore.messages.PrefixHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Core implements CommandExecutor {

    private final GodllyCore plugin;
    private final FullInventoryListener fullInventoryListener;
    public Core(GodllyCore plugin, FullInventoryListener fullInventoryListener) {
        this.plugin = plugin;
        this.fullInventoryListener = fullInventoryListener;
        startAutoClearLag();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(getPrefix() + "§cPlease provide a subcommand.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                handleReload(sender);
                break;
            case "info":
                handleInfo(sender);
                break;
            case "clearlag":
                handleClearLag(sender);
                break;
            case "restart":
                if (args.length > 1) {
                    handleRestart(sender, args[1]);
                } else {
                    sender.sendMessage(getPrefix() + "§cPlease specify a time for the restart.");
                }
                break;
            default:
                sender.sendMessage(getPrefix() + "§cUnknown subcommand. Try /core reload, /core info, or /core clearlag.");
                break;
        }
        return true;
    }

    private void handleReload(CommandSender sender) {
        if (sender instanceof Player player && !player.hasPermission("godllycore.core.reload")) {
            player.sendMessage(getPrefix() + "§cYou don't have permission to reload the configuration.");
            return;
        }

        long startTime = System.currentTimeMillis();

        plugin.reloadConfig();
        PrefixHandler.loadPrefix(); // Initialize prefix system

        fullInventoryListener.loadConfig();

        // Unregister all event listeners to prevent duplicates
        HandlerList.unregisterAll(plugin);

        // Re-register event listeners using stored instances (not new ones)
        List<Listener> listeners = Arrays.asList(
                plugin.getAfkListener(),
                plugin.getCommandSpyListener(),
                plugin.getGodModeListener(),
                plugin.getTeleportListener(),
                new MuteChatListener(plugin),
                new ChatFilterListener(plugin),
                new JoinQuitListener(plugin),
                new CustomDeathMessageListener(plugin),
                new PlayerRespawnListener(plugin),
                new FirstJoinListener(plugin),
                new EssentialCommand(plugin),
                new FullInventoryListener(plugin),
                new ItemDropListener(plugin.getDropCommand()),
                new CommandListener(),
                new FallingBlockListener()
        );

        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        sender.sendMessage(getPrefix() + "§aConfiguration reloaded successfully in " + elapsedTime + "ms!");
    }

    private void handleInfo(CommandSender sender) {
        String version = plugin.getDescription().getVersion();
        String author = plugin.getDescription().getAuthors().getFirst();
        sender.sendMessage(" ");
        sender.sendMessage("§8--------[§3GodllyCore Info§8]--------");
        sender.sendMessage("§3Version: §e" + version);
        sender.sendMessage("§3Developer: §e" + author);
        sender.sendMessage(" ");
        sender.sendMessage("§aA Custom plugin for GodllyBox!");
        sender.sendMessage("§8-------------------------------------");
    }

    private void handleClearLag(CommandSender sender) {
        if (sender instanceof Player player && !player.hasPermission("godllycore.core.clearlag")) {
            player.sendMessage(getPrefix() + "§cYou don't have permission to execute this command.");
            return;
        }

        Bukkit.broadcastMessage(getPrefix() + "§4Clearing all entities in 5 seconds!");
        new BukkitRunnable() {
            @Override
            public void run() {
                clearLag();
            }
        }.runTaskLater(plugin, 100L);
    }

    private void startAutoClearLag() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(getPrefix() + "§4Automatic entity clear in 10 seconds!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        clearLag();
                    }
                }.runTaskLater(plugin, 200L);
            }
        }.runTaskTimer(plugin, 36000L, 36000L);
    }

    private void clearLag() {
        int removed = 0;
        for (org.bukkit.World world : Bukkit.getWorlds()) { // Loop through all worlds
            for (Entity entity : world.getEntities()) {
                if (entity instanceof org.bukkit.entity.Item) { // Ensure it's a dropped item
                    entity.remove();
                    removed++;
                }
            }
        }
        Bukkit.broadcastMessage(getPrefix() + "§cCleared §4" + removed + " §centities (dropped items)!");
    }


    private void handleRestart(CommandSender sender, String timeArg) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPrefix() + "§cOnly players can execute this command.");
            return;
        }
        if (!player.hasPermission("godllycore.core.restart")) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            player.sendMessage(getPrefix() + "§cYou don't have permission to restart the server.");
            return;
        }
        int time;
        try {
            time = Integer.parseInt(timeArg);
        } catch (NumberFormatException e) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            player.sendMessage(getPrefix() + "§cInvalid time format. Please enter a valid number.");
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        player.sendMessage(getPrefix() + "§aThe server will restart in " + time + " seconds.");
        new BukkitRunnable() {
            int countdown = time;
            @Override
            public void run() {
                if (countdown <= 0) {
                    Bukkit.getServer().shutdown();
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        try {
                            Runtime.getRuntime().exec("java -jar " + plugin.getDataFolder().getParentFile().getName() + "/server.jar");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }, 20L);
                    cancel();
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getPrefix() + "§erestarting in §6" + countdown + "§e seconds..."));
                    }
                    countdown--;
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
