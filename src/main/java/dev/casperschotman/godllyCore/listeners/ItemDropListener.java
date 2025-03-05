package dev.casperschotman.godllyCore.listeners;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import dev.casperschotman.godllyCore.commands.DropCommand;

import java.util.UUID;

public class ItemDropListener implements Listener {

    private final DropCommand dropCommand;

    public ItemDropListener(DropCommand dropCommand) {
        this.dropCommand = dropCommand;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Check if the player has bypass enabled
        if (dropCommand.hasBypass(playerUUID)) {
            return; // Allow the item drop
        }

        // Check if the player has an active cooldown
        long cooldownTime = dropCommand.getPlayerCooldown(playerUUID);

        // If the cooldown is still active, allow the item drop
        if (System.currentTimeMillis() < cooldownTime) {
            return;
        }

        // If the cooldown has expired, cancel the item drop and send a message
        event.setCancelled(true);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
        player.sendMessage(getPrefix() + "§cYou can't drop items right now.");
        player.sendMessage(getPrefix() + "§cUse §4/drop§c to enable item dropping.");
    }
}
