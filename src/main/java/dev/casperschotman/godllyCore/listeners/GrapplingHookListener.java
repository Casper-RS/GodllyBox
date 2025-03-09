package dev.casperschotman.godllyCore.listeners;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GrapplingHookListener implements Listener {

	private final GodllyCore plugin;
	private static final String HOOK_NAME = "Grappling Hook";
	public GrapplingHookListener(GodllyCore plugin) {
		this.plugin = plugin;

	}

	@EventHandler
	public void onHookLand(PlayerFishEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (!isGrapplingHook(item)) {
			return;
		}

		if(event.getState() == PlayerFishEvent.State.IN_GROUND) {
			FishHook hook = event.getHook();
			Location hookLocation = hook.getLocation();

			new BukkitRunnable(){
				@Override
				public void run() {
					if (player.isOnline() && hook.isValid()) {
						applySwingingMotion(player, hookLocation);
					} else {
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 1);
		}
	}

	private boolean isGrapplingHook(ItemStack item) {
		if (item == null || !item.hasItemMeta()) {
			return false;
		}
		ItemMeta meta = item.getItemMeta();
		return meta.hasDisplayName() && meta.getDisplayName().equals(HOOK_NAME);
	}

	private void applySwingingMotion(Player player, Location anchor){
		Location playerLocation = player.getLocation();
		Vector direction = anchor.toVector().subtract(playerLocation.toVector());
		Vector velocity = direction.normalize().multiply(0.6);

		velocity.setY(velocity.getY() + 0.1);
		player.setVelocity(velocity);

		player.getWorld().spawnParticle(Particle.CLOUD, playerLocation,  5);
		player.playSound(playerLocation, Sound.ENTITY_ZOMBIE_INFECT, 1.0f, 1.0f);
	}
}
