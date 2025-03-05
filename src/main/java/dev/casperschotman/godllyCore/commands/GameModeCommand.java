package dev.casperschotman.godllyCore.commands;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
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

        Player target = player;
        boolean changeOthers = args.length > 0 && player.hasPermission("godllycore.gamemode.others");

        if (changeOthers) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                player.sendMessage(getPrefix() + "§cPlayer not found.");
                return true;
            }
        }

        GameMode gameMode = switch (command.getName().toLowerCase()) {
            case "gmc" -> GameMode.CREATIVE;
            case "gms" -> GameMode.SURVIVAL;
            case "gmsp" -> GameMode.SPECTATOR;
            case "gma" -> GameMode.ADVENTURE;
            default -> null;
        };

        if (gameMode == null) return false;

        if (player.hasPermission("godllycore.gamemode." + gameMode.name().toLowerCase()) ||
                player.hasPermission("godllycore.gamemode.*") ||
                changeOthers) {

            target.setGameMode(gameMode);
            target.sendMessage(getPrefix() + "§aYour game mode has been set to §6" + gameMode.name() + "§a.");

            if (changeOthers) {
                player.sendMessage(getPrefix() + "§aYou set §6" + target.getName() + "§a's game mode to §6" + gameMode.name() + "§a.");
            }

        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            player.sendMessage(getPrefix() + "§cYou don't have permission to use this command.");
        }
        return true;
    }
}
