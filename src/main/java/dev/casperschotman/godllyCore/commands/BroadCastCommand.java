package dev.casperschotman.godllyCore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class BroadCastCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("godllycore.broadcast")) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Usage: /broadcast <message>");
			return true;
		}

		// Combine arguments into a full message
		String message = String.join(" ", args);

		// Add color support
		message = ChatColor.translateAlternateColorCodes('&', message);

		// Broadcast to all players
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(getPrefix() + ChatColor.RESET + message);
		Bukkit.broadcastMessage(" ");

		return true;
	}
}

