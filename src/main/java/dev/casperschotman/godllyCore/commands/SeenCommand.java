package dev.casperschotman.godllyCore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class SeenCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.hasPermission("godllycore.seen.use")) {
			sender.sendMessage(getPrefix() + ChatColor.RED + "You don't have permission to use this command.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(getPrefix() + ChatColor.RED + "Usage: /seen <playerName or UUID>");
			return true;
		}

		String input = args[0];
		OfflinePlayer target;

		// Check if input is a UUID
		try {
			UUID uuid = UUID.fromString(input);
			target = Bukkit.getOfflinePlayer(uuid);
		} catch (IllegalArgumentException e) {
			// Not a UUID, so assume it's a player name
			target = Bukkit.getOfflinePlayer(input);
		}

		if (target == null) {
			sender.sendMessage(getPrefix() + ChatColor.RED + "Player not found.");
			return true;
		}

		if (target.isOnline()) {
			sender.sendMessage(getPrefix() + ChatColor.GREEN + target.getName() + " is currently online.");
			return true;
		}

		long lastPlayed = target.getLastPlayed();
		if (lastPlayed == 0) {
			sender.sendMessage(getPrefix() + ChatColor.RED + target.getName() + " has never joined the server.");
			return true;
		}

		String lastSeenTime = formatTimeDifference(System.currentTimeMillis() - lastPlayed);
		sender.sendMessage(getPrefix() + ChatColor.YELLOW + target.getName() + " was last seen " + lastSeenTime + " ago.");

		return true;
	}

	private String formatTimeDifference(long millis) {
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		if (days > 0) return days + " day(s) " + (hours % 24) + " hour(s)";
		if (hours > 0) return hours + " hour(s) " + (minutes % 60) + " minute(s)";
		if (minutes > 0) return minutes + " minute(s) " + (seconds % 60) + " second(s)";
		return seconds + " second(s)";
	}
}
