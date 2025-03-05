package dev.casperschotman.godllyCore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import dev.casperschotman.godllyCore.GodllyCore;

import java.util.HashMap;
import java.util.UUID;

public class FullInventoryListener implements Listener {
	private final GodllyCore plugin;
	private final NamespacedKey toggleKey;
	private final HashMap<UUID, Long> cooldowns = new HashMap<>();
	private long cooldownTime; // Store configurable cooldown time

	public FullInventoryListener(GodllyCore plugin) {
		this.plugin = plugin;
		this.toggleKey = new NamespacedKey(plugin, "invFullTitle");
		loadConfig(); // Load the cooldown from config
	}

	public void loadConfig() {
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		this.cooldownTime = config.getLong("inv-full-message-cooldown", 5000); // Default: 5000ms (5 sec)
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();

		if (isInventoryFull(player)) {
			event.setCancelled(true);
			showFullInventoryMessage(player);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack droppedItem = new ItemStack(event.getBlock().getType());

		if (isInventoryFull(player) && !canFitItem(player, droppedItem)) {
			event.setCancelled(true);
			showFullInventoryMessage(player);
		}
	}

	private boolean isInventoryFull(Player player) {
		return player.getInventory().firstEmpty() == -1;
	}

	private boolean canFitItem(Player player, ItemStack item) {
		return player.getInventory().addItem(item).isEmpty();
	}

	private void showFullInventoryMessage(Player player) {
		PersistentDataContainer data = player.getPersistentDataContainer();
		boolean showTitle = data.getOrDefault(toggleKey, PersistentDataType.BYTE, (byte) 1) == 1;

		long currentTime = System.currentTimeMillis();
		if (cooldowns.containsKey(player.getUniqueId())) {
			long lastMessageTime = cooldowns.get(player.getUniqueId());
			if (currentTime - lastMessageTime < cooldownTime) {
				return; // Prevent spam
			}
		}
		cooldowns.put(player.getUniqueId(), currentTime); // Update cooldown

		if (showTitle) {
			player.sendTitle(
					ChatColor.RED + "Inventory Full!",
					ChatColor.YELLOW + "Clear space to collect more items.",
					10, 40, 10
			);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
		}
	}
}
