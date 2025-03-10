package dev.casperschotman.godllyCore.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.entity.FallingBlock;

public class FallingBlockListener implements Listener {

	@EventHandler
	public void onFallingBlockSpawn(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof FallingBlock) {
			event.setCancelled(true); // Cancel the event, preventing the block from falling
		}
	}
}
