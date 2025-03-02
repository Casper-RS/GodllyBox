package dev.casperschotman.godllyCore.listeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    // List of commands to block
    private final String[] blockedCommands = {
            "/pl", "/plugins", "/bukkit:plugins", "/reload", "/bukkit:reload",
            "/?", "/bukkit:?", "/bukkit:help", "/help", "/minecraft:me"
    };

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0].toLowerCase();  // Get the command (first part)

        // Check if the command is in the blocked list
        for (String blockedCommand : blockedCommands) {
            if (command.equalsIgnoreCase(blockedCommand)) {
                if (!event.getPlayer().hasPermission("godllybox.viewplugins")) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§cYou do not have permission to use this command.");
                    return;
                }
            }
        }
    }
}
