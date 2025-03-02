package dev.casperschotman.godllyCore.commands;
import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage(getPrefix() + "§cFly mode disabled.");
            } else {
                player.setAllowFlight(true);
                player.sendMessage(getPrefix() + "§aFly mode enabled.");
            }
        } else {
            sender.sendMessage(getPrefix() + "§cThis command can only be used by a player.");
        }
        return true;
    }
}

