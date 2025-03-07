package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.*;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class SpyCommand implements CommandExecutor, TabCompleter {

    private final Set<UUID> spyingPlayers = new HashSet<>();
    private final Set<UUID> exemptPlayers = new HashSet<>();
    private final GodllyCore plugin;

    public SpyCommand(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPrefix() + "§cOnly players can use this command.");
            return true;
        }

        if (args.length == 0) {
            toggleSpy(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "on":
                enableSpy(player);
                break;
            case "off":
                disableSpy(player);
                break;
            case "exempt":
                if (args.length > 1) {
                    if (player.hasPermission("godllycore.cspy.admin")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            toggleExempt(target, player);
                        } else {
                            player.sendMessage(getPrefix() + "§cPlayer not found.");
                        }
                    } else {
                        player.sendMessage(getPrefix() + "§cYou don't have permission for this.");
                    }
                } else {
                    player.sendMessage(getPrefix() + "§cSpecify a player.");
                }
                break;
            default:
                player.sendMessage(getPrefix() + "§cUnknown subcommand. Use /cspy [on/off/exempt] [player].");
                break;
        }
        return true;
    }

    private void toggleSpy(Player player) {
        if (spyingPlayers.contains(player.getUniqueId())) {
            disableSpy(player);
        } else {
            enableSpy(player);
        }
    }

    private void enableSpy(Player player) {
        spyingPlayers.add(player.getUniqueId());
        player.sendMessage(getPrefix() + "§7Command spy is now §aENABLED.");
    }

    private void disableSpy(Player player) {
        spyingPlayers.remove(player.getUniqueId());
        player.sendMessage(getPrefix() + "§7Command spy is now §cDISABLED.");
    }

    private void toggleExempt(Player target, Player sender) {
        UUID targetUUID = target.getUniqueId();
        if (exemptPlayers.contains(targetUUID)) {
            exemptPlayers.remove(targetUUID);
            sender.sendMessage(getPrefix() + "§7" + target.getName() + " is no longer exempted from command spy.");
        } else {
            exemptPlayers.add(targetUUID);
            sender.sendMessage(getPrefix() + "§7" + target.getName() + " is now exempted from command spy.");
        }
    }

    public void handleCommand(Player sender, String command) {
        if (sender.hasPermission("op")) {
            broadcastSpyMessage("§cCONSOLE§8: §c/" + command);
        } else {
            broadcastSpyMessage("§c" + sender.getName() + "§8: §c/" + command, sender);
        }
    }

    private void broadcastSpyMessage(String message, Player sender) {
        for (UUID uuid : spyingPlayers) {
            Player spy = Bukkit.getPlayer(uuid);
            if (spy != null && spy.hasPermission("command.spy") && !exemptPlayers.contains(sender.getUniqueId())) {
                spy.sendMessage(getPrefix() + "§8[§cCSPY§8] " + message);
            }
        }
    }

    private void broadcastSpyMessage(String message) {
        for (UUID uuid : spyingPlayers) {
            Player spy = Bukkit.getPlayer(uuid);
            if (spy != null && spy.hasPermission("command.spy")) {
                spy.sendMessage(getPrefix() + "§8[§cCSPY§8] " + message);
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("on", "off", "exempt");
        }
        return Collections.emptyList();
    }
}
