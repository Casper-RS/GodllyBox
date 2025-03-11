package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.messages.PrefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class ShoutCommand implements CommandExecutor {

	private final HashMap<UUID, Long> cooldowns = new HashMap<>();
	private final long cooldownTime = 10 * 60 * 1000; // 10 minutes in milliseconds

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		}

		if (!player.hasPermission("godllycore.shout")) {
			player.sendMessage(getPrefix() + ChatColor.RED + "You don't have the correct permissions.");
			return true;
		}

		if (cooldowns.containsKey(player.getUniqueId())) {
			long timeLeft = (cooldowns.get(player.getUniqueId()) + cooldownTime) - System.currentTimeMillis();
			if (timeLeft > 0) {
				long minutes = timeLeft / 60000;
				long seconds = (timeLeft / 1000) % 60;
				player.sendMessage(getPrefix() + ChatColor.RED + "Please wait " + ChatColor.GREEN + minutes + "m " + seconds + "s" + ChatColor.RED + " to use this command again!");
				return true;
			}
		}

		if (args.length == 0) {
			player.sendMessage(getPrefix() + ChatColor.RED + "Incorrect Usage: " + ChatColor.GRAY + "/shout (message)");
			return true;
		}

		// Combine all arguments into a single message
		String message = String.join(" ", args);

		// Play sound for all players
		Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1000, 1));

		// Send formatted message
		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SHOUT!");
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "> " + ChatColor.YELLOW + ChatColor.BOLD + player.getName() + ChatColor.WHITE + ": " + message);
		Bukkit.broadcastMessage(" ");

		// Set cooldown
		cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

		return true;
	}
}
