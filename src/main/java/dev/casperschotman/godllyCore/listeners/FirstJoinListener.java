package dev.casperschotman.godllyCore.listeners;
import dev.casperschotman.godllyCore.GodllyCore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FirstJoinListener implements Listener {

    private final GodllyCore plugin;

    public FirstJoinListener(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        if (!config.getBoolean("first-join.enabled")) return;

        // Check if it's the player's first join
        if (!player.hasPlayedBefore()) {
            giveStarterItems(player);
            sendJoinMessage(player);
        }
    }

    private void giveStarterItems(Player player) {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection itemsSection = config.getConfigurationSection("first-join.items");

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                Material material = Material.getMaterial(itemsSection.getString(key + ".material", "STONE"));
                String name = ChatColor.translateAlternateColorCodes('&', itemsSection.getString(key + ".name", "&7Starter Item"));

                if (material != null) {
                    ItemStack item = new ItemStack(material, 1);
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        meta.setDisplayName(name);
                        item.setItemMeta(meta);
                    }
                    player.getInventory().addItem(item);
                }
            }
        }
    }

    private void sendJoinMessage(Player player) {
        FileConfiguration config = plugin.getConfig();
        String joinMessage = config.getString("first-join.join-message", "&bWelcome %player%!");

        joinMessage = ChatColor.translateAlternateColorCodes('&', joinMessage.replace("%player%", player.getName()));

        Bukkit.broadcastMessage(joinMessage);
    }
}
