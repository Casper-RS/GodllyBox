package dev.casperschotman.godllyCore.commands;
import dev.casperschotman.godllyCore.GodllyCore;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class InvFullToggleCommand implements CommandExecutor {
	private final GodllyCore plugin;
	private final NamespacedKey toggleKey;

	public InvFullToggleCommand(GodllyCore plugin) {
		this.plugin = plugin;
		this.toggleKey = new NamespacedKey(plugin, "invFullTitle");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command.");
			return true;
		}

		PersistentDataContainer data = player.getPersistentDataContainer();
		boolean isEnabled = data.getOrDefault(toggleKey, PersistentDataType.BYTE, (byte) 1) == 1;

		// Toggle state
		boolean newState = !isEnabled;
		data.set(toggleKey, PersistentDataType.BYTE, newState ? (byte) 1 : (byte) 0);

		player.sendMessage(getPrefix() + ChatColor.GREEN + "Inventory full title notifications are now " +
				(newState ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED") + ChatColor.GREEN + ".");
		return true;
	}
}
