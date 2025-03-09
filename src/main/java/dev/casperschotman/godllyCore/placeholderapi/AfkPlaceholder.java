package dev.casperschotman.godllyCore.placeholderapi;

import dev.casperschotman.godllyCore.GodllyCore;
import dev.casperschotman.godllyCore.listeners.AfkListener;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class AfkPlaceholder extends PlaceholderExpansion {

	private final GodllyCore plugin;
	private final AfkListener afkListener;

	public AfkPlaceholder(GodllyCore plugin, AfkListener afkListener) {
		this.plugin = plugin;
		this.afkListener = afkListener;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "godllycore";
	}

	@Override
	public @NotNull String getAuthor() {
		return plugin.getDescription().getAuthors().get(0);
	}

	@Override
	public @NotNull String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String onRequest(OfflinePlayer player, @NotNull String identifier) {
		if (player == null || player.getPlayer() == null) {
			return "";
		}

		// If placeholder is %godllycore_afk%
		if (identifier.equalsIgnoreCase("afk")) {
			return afkListener.isAfk(player.getPlayer()) ? "true" : "false";
		}

		return null;
	}
}
