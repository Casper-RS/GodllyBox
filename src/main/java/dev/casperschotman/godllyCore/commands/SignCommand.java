package dev.casperschotman.godllyCore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class SignCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(getPrefix() + ChatColor.RED + "Only players can use this command!");
			return true;
		}

		if (!player.hasPermission("godllycore.staffsign")) {
			player.sendMessage(getPrefix() + ChatColor.RED + "You don't have permission to use this command!");
			return true;
		}

		ItemStack heldItem = player.getInventory().getItemInMainHand();
		if (heldItem.getType() != Material.BLUE_CANDLE && heldItem.getType() != Material.TURTLE_EGG) {
			player.sendMessage(getPrefix() + ChatColor.RED + "You cannot sign this item.");
			return true;
		}

		ItemMeta meta = heldItem.getItemMeta();
		if (meta == null) return true;

		List<String> lore = meta.hasLore() ? meta.getLore() : new java.util.ArrayList<>();
		String signature = ChatColor.WHITE + "● " + ChatColor.GOLD + ChatColor.BOLD + player.getName();

		if (lore.contains(signature)) {
			player.sendMessage(getPrefix() + ChatColor.RED + "You cannot sign more than once!");
			return true;
		}

		lore.add(" ");
		lore.add(signature);
		meta.setLore(lore);
		heldItem.setItemMeta(meta);

		player.sendMessage(getPrefix() + ChatColor.GREEN + "Your signature has been added!");
		return true;
	}
}
