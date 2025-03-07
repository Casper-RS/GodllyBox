package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final GodllyCore plugin;

    public SpawnCommand(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        Location spawnLocation = getSpawnLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            player.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            player.sendMessage(ChatColor.RED + "Spawn location is not set in the config!");
        }
        return true;
    }

    private Location getSpawnLocation() {
        plugin.reloadConfig(); // Reload config to get latest spawn values
        return plugin.getConfig().contains("death.spawn") ? new Location(
                Bukkit.getWorld(plugin.getConfig().getString("death.spawn.world", "new1")),
                plugin.getConfig().getDouble("death.spawn.x", 4.5),
                plugin.getConfig().getDouble("death.spawn.y", 59),
                plugin.getConfig().getDouble("death.spawn.z", 3.5),
                (float) plugin.getConfig().getDouble("death.spawn.yaw", 90),
                (float) plugin.getConfig().getDouble("death.spawn.pitch", 0)
        ) : null;
    }
}
