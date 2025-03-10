package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import dev.casperschotman.godllyCore.listeners.MuteChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class ChatManager implements CommandExecutor {

	private final GodllyCore plugin;

	public ChatManager(GodllyCore plugin) {
		this.plugin = plugin;
		plugin.getCommand("clearchat").setExecutor(this);
		plugin.getCommand("mutechat").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		}

		switch (command.getName().toLowerCase()) {
			case "clearchat":
			case "cc":
				if (!player.hasPermission("godllycore.chat.clear")) {
					player.sendMessage(getPrefix() + ChatColor.RED + "You don't have permission to use this command!");
					return true;
				}
				clearChat();
				Bukkit.broadcastMessage(getPrefix() + ChatColor.GREEN + "-------------------------------");
				Bukkit.broadcastMessage(getPrefix() + ChatColor.GREEN + "Chat has been cleared by " + ChatColor.YELLOW + player.getName());
				Bukkit.broadcastMessage(getPrefix() + ChatColor.GREEN + "-------------------------------");
				break;

			case "mutechat":
				if (!player.hasPermission("godllycore.chat.mute")) {
					player.sendMessage(getPrefix() + ChatColor.RED + "You don't have permission to use this command!");
					return true;
				}
				MuteChatListener.toggleChatMute();
				String status = MuteChatListener.isChatMuted() ? ChatColor.RED + "muted" : ChatColor.GREEN + "unmuted";
				Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " has " + status + " the chat.");
				break;

			default:
				return false;
		}
		return true;
	}

	private void clearChat() {
		for (int i = 0; i < 500; i++) {
			Bukkit.broadcastMessage(" ");
		}
	}
}
