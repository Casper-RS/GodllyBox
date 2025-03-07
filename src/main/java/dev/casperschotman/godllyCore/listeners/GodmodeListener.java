package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class GodmodeListener implements Listener {
    private final GodllyCore plugin;
    private final Set<UUID> godModePlayers = new HashSet<>();

    public GodmodeListener(GodllyCore plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (godModePlayers.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    public void toggleGodMode(Player player) {
        UUID playerId = player.getUniqueId();
        if (godModePlayers.contains(playerId)) {
            godModePlayers.remove(playerId);
            player.sendMessage(getPrefix() + "§cGodmode disabled.");
        } else {
            godModePlayers.add(playerId);
            player.sendMessage(getPrefix() + "§aGodmode enabled.");
        }
    }
}
