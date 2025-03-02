package dev.casperschotman.godllyCore.commands;
import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPrefix() + "§cOnly players can use this command!");
            return true;
        }

        // Check permissions for each command
        switch (command.getName().toLowerCase()) {
            case "gmc":
                if (player.hasPermission("godllycore.gamemode.creative") || player.hasPermission("godllycore.gamemode.*")) {
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(getPrefix() + "§aGame mode set to §6Creative.");
                } else {
                    player.sendMessage(getPrefix() + "§cYou don't have permission to use this command.");
                }
                break;
            case "gms":
                if (player.hasPermission("godllycore.gamemode.survival") || player.hasPermission("godllycore.gamemode.*")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(getPrefix() + "§aGame mode set to §6Survival.");
                } else {
                    player.sendMessage(getPrefix() + "§cYou don't have permission to use this command.");
                }
                break;
            case "gmsp":
                if (player.hasPermission("godllycore.gamemode.spectator") || player.hasPermission("godllycore.gamemode.*")) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.sendMessage(getPrefix() + "§aGame mode set to §6Spectator.");
                } else {
                    player.sendMessage(getPrefix() + "§cYou don't have permission to use this command.");
                }
                break;
            case "gma":
                if (player.hasPermission("godllycore.gamemode.adventure") || player.hasPermission("godllycore.gamemode.*")) {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.sendMessage(getPrefix() + "§aGame mode set to §6Adventure.");
                } else {
                    player.sendMessage(getPrefix() + "§cYou don't have permission to use this command.");
                }
                break;
            default:
                return false;
        }
        return true;
    }
}
