package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import dev.casperschotman.godllyCore.messages.PrefixHandler;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteChatListener implements Listener {

	private static boolean chatMuted = false;
	private final GodllyCore plugin;

	public MuteChatListener(GodllyCore plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static void toggleChatMute() {
		chatMuted = !chatMuted;
	}

	public static boolean isChatMuted() {
		return chatMuted;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (chatMuted && !event.getPlayer().hasPermission("godllycore.chat.bypass")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(PrefixHandler.getPrefix() + ChatColor.RED + "Chat is currently muted!");
		}
	}
}
