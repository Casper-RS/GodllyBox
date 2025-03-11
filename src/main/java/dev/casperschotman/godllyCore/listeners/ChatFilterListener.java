package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import dev.casperschotman.godllyCore.messages.PrefixHandler;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ChatFilterListener implements Listener {

	private final GodllyCore plugin;
	private List<String> blockedWords;
	private boolean filterEnabled;
	private String warningMessage;

	public ChatFilterListener(GodllyCore plugin) {
		this.plugin = plugin;
		loadFilterConfig();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void loadFilterConfig() {
		FileConfiguration config = plugin.getConfig();
		filterEnabled = config.getBoolean("chat-filter.enabled", true);
		blockedWords = config.getStringList("chat-filter.blocked-words")
				.stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList());
		warningMessage = ChatColor.translateAlternateColorCodes('&', config.getString("chat-filter.warning-message", "&cYour message contains a blocked word!"));
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (!filterEnabled) return;

		String message = event.getMessage().toLowerCase().replaceAll("[^a-zA-Z]", ""); // Remove special characters

		for (String word : blockedWords) {
			Pattern pattern = Pattern.compile(".*" + Pattern.quote(word) + ".*", Pattern.CASE_INSENSITIVE);
			if (pattern.matcher(message).matches()) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(PrefixHandler.getPrefix() + warningMessage);
				return;
			}
		}
	}
}
