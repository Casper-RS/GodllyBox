package dev.casperschotman.godllyCore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OffhandCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		}

		// Get the items in main hand and offhand
		ItemStack mainHandItem = player.getInventory().getItemInMainHand();
		ItemStack offHandItem = player.getInventory().getItemInOffHand();

		// Swap the items
		player.getInventory().setItemInMainHand(offHandItem);
		player.getInventory().setItemInOffHand(mainHandItem);

		player.sendMessage(ChatColor.GREEN + "Swapped your main hand and offhand items!");

		return true;
	}
}
