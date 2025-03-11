package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.messages.PrefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class PrefixCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(getPrefix() + ChatColor.RED + "Only players can use this command!");
			return true;
		}

		if (!player.hasPermission("godllycore.customprefix")) {
			player.sendMessage(getPrefix() + ChatColor.RED + "You don't have the correct permissions.");
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(ChatColor.RED + "Please define 'set' or 'remove' as the first argument.");
			return true;
		}

		String action = args[0].toLowerCase();

		if (action.equals("set")) {
			if (args.length < 2) {
				player.sendMessage(ChatColor.RED + "You need to define your prefix!");
				return true;
			}

			String prefix = args[1];
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setprefix 100 " + prefix);
			player.sendMessage(getPrefix() + ChatColor.GREEN + "Your prefix has been set to: " + ChatColor.RESET + prefix);
		}

		else if (action.equals("remove")) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta clear");
			player.sendMessage(getPrefix() + ChatColor.GREEN + "You have successfully removed your prefix!");
		}

		else {
			player.sendMessage(ChatColor.RED + "Invalid argument! Use 'set' or 'remove'.");
		}

		return true;
	}
}
