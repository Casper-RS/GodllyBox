package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class CustomDeathMessageListener implements Listener {

    private final GodllyCore plugin;
    private final Random random = new Random();

    public CustomDeathMessageListener(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = victim.getKiller();
        String deathMessage = "";

        FileConfiguration config = plugin.getConfig();
        event.setDeathMessage(null); // Clear default death message

        if (victim.getLastDamageCause() != null && victim.getLastDamageCause().getCause().equals(org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL)) {
            deathMessage = ChatColor.translateAlternateColorCodes('&',
                            config.getString("messages.death.fall", "§c§kiii§r§c%player% really died to fall damage what a loser!§c§kiii§r§c"))
                    .replace("%player%", victim.getName());
        }

        else if (victim.getLastDamageCause() != null && victim.getLastDamageCause().getCause().equals(org.bukkit.event.entity.EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            deathMessage = ChatColor.translateAlternateColorCodes('&',
                            config.getString("messages.death.explosion", "§c§kiii§r§c%player% really blew up without saying goodbye!§c§kiii§r§c"))
                    .replace("%player%", victim.getName());
        }

        // Handle PvP death
        else if (attacker != null) {
            ItemStack weapon = attacker.getInventory().getItemInMainHand();
            String weaponName = weapon.getType() != Material.AIR ?
                    (weapon.hasItemMeta() && weapon.getItemMeta().hasDisplayName() ?
                            weapon.getItemMeta().getDisplayName() : weapon.getType().name().toLowerCase().replace("_", " "))
                    : "their Fists";

            List<String> pvpMessages = config.getStringList("messages.death.pvp");
            if (!pvpMessages.isEmpty()) {
                deathMessage = ChatColor.translateAlternateColorCodes('&', pvpMessages.get(random.nextInt(pvpMessages.size())));
            } else {
                deathMessage = "§c§kiii§r§c%player% got killed by %attacker%!§r§c§kiii";
            }

            deathMessage = deathMessage.replace("%player%", victim.getName()).replace("%attacker%", attacker.getName()).replace("%weapon%", weaponName);
        }

        // Send formatted message
        if (!deathMessage.isEmpty()) {
            Bukkit.broadcastMessage(deathMessage);
        }
    }

}
