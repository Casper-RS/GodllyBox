package dev.casperschotman.godllyCore.commands;
import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TeleportCommand implements CommandExecutor, TabCompleter {

    // Store pending teleport requests
    private final HashMap<UUID, UUID> teleportRequests = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPrefix() + "§cOnly players can use this command!");
            return true;
        }

        // Check for specific permissions for tp, tphere, and tpall
        switch (command.getName().toLowerCase()) {
            case "tp":
                if (!player.hasPermission("godllybox.teleport.tp") && !player.hasPermission("godllybox.teleport.*")) {
                    player.sendMessage(getPrefix() + "§cYou do not have permission to use this command!");
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                    return true;
                }
                break;

            case "tphere":
                if (!player.hasPermission("godllybox.teleport.tphere") && !player.hasPermission("godllybox.teleport.*")) {
                    player.sendMessage(getPrefix() + "§cYou do not have permission to use this command!");
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                    return true;
                }
                break;

            case "tpall":
                if (!player.hasPermission("godllybox.teleport.tpall") && !player.hasPermission("godllybox.teleport.*")) {
                    player.sendMessage(getPrefix() + "§cYou do not have permission to use this command!");
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                    return true;
                }
                break;

            default:
                break;
        }

        switch (command.getName().toLowerCase()) {
            case "tp":
                // /tp <player>
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (player.equals(target)) {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            player.sendMessage(getPrefix() + "§cYou cannot teleport to yourself!");
                            return true;
                        }
                        player.teleport(target.getLocation());
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.sendMessage(getPrefix() + "§aTeleported to §e" + target.getName());
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        player.sendMessage(getPrefix() + "§cPlayer not found!");
                    }
                }
                break;

            case "tphere":
                // /tphere <player>
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (player.equals(target)) {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                            player.sendMessage(getPrefix() + "§cYou cannot teleport yourself to yourself!");
                            return true;
                        }
                        target.teleport(player.getLocation());
                        target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.sendMessage(getPrefix() + "§e" + target.getName() + "§a has been teleported to you.");
                        target.sendMessage(getPrefix() + "§aYou have been teleported to §e" + player.getName());
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                        player.sendMessage(getPrefix() + "§cPlayer not found!");
                    }
                }
                break;

            case "tpall":
                // /tpall - Teleport all players to the sender
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayer.equals(player)) {
                        onlinePlayer.teleport(player.getLocation());
                        onlinePlayer.sendMessage(getPrefix() + "§aYou have been teleported to §e" + player.getName());
                        onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    }
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                player.sendMessage(getPrefix() + "§aAll players have been teleported to you.");
                break;

            case "tpa":
                // /tpa <player> - Request to teleport to another player
                if (args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        teleportRequests.put(target.getUniqueId(), player.getUniqueId()); // Store the request
                        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        target.sendMessage(getPrefix() + "§e" + player.getName() + " §ahas requested to teleport to you.");
                        target.sendMessage(getPrefix() + "§aUse §e/tpaccept §aor §e/tpadeny§a.");
                        player.sendMessage(getPrefix() + "§aTeleport request sent to §e" + target.getName());
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        player.sendMessage(getPrefix() + "§cPlayer not found!");
                    }
                }
                break;

            case "tpaccept":
                // /tpaccept - Accept the teleport request
                UUID requesterUUID = teleportRequests.get(player.getUniqueId());
                if (requesterUUID != null) {
                    Player requester = Bukkit.getPlayer(requesterUUID);
                    if (requester != null) {
                        requester.teleport(player.getLocation());
                        requester.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f); // Play level up sound
                        requester.sendMessage(getPrefix() + "§aYour teleport request was accepted. §eTeleporting...");
                        player.sendMessage(getPrefix() + "§aYou accepted the teleport request from §e" + requester.getName());
                    }
                    teleportRequests.remove(player.getUniqueId());
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f); // Play level up sound
                    player.sendMessage(getPrefix() + "§cYou don't have any pending teleport requests.");
                }
                break;

            case "tpadeny":
                // /tpadeny - Deny the teleport request
                UUID requesterUUIDDeny = teleportRequests.get(player.getUniqueId());
                if (requesterUUIDDeny != null) {
                    Player requester = Bukkit.getPlayer(requesterUUIDDeny);
                    if (requester != null) {
                        requester.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                        requester.sendMessage(getPrefix() + "§aYour teleport request was §cdenied.");
                        player.sendMessage(getPrefix() + "§aYou §cdenied §athe teleport request from §e" + requester.getName());
                    }
                    teleportRequests.remove(player.getUniqueId());
                } else {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                    player.sendMessage(getPrefix() + "§cYou don't have any pending teleport requests.");
                }
                break;

            default:
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("tp") || command.getName().equalsIgnoreCase("tphere") || command.getName().equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                List<String> playerNames = new ArrayList<>();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    playerNames.add(onlinePlayer.getName());
                }
                return playerNames;  // Return a list of online player names
            }
        }
        return null;  // No tab completion for other commands
    }
}
