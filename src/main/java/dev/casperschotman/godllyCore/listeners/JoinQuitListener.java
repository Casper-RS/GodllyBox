package dev.casperschotman.godllyCore.listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.casperschotman.godllyCore.GodllyCore;

public class JoinQuitListener implements Listener {

    private final GodllyCore plugin;

    public JoinQuitListener(GodllyCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        // Get configurable join message
        String joinMessage = config.getString("messages.join", "§7[§a→§7] §a%player%")
                .replace("%player%", player.getName());
        event.setJoinMessage(joinMessage);

        // Send welcome message after 2 seconds
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.sendMessage("§x§0§F§B§4§F§B§m===============================");
            player.sendMessage(" ");
            player.sendMessage("              §d§lWelcome To GodllyBox!");
            player.sendMessage(" ");
            player.sendMessage("  §f●  §e§lGrindly §f⛏");
            player.sendMessage("  §f●  §c§lPrestiges §f🗡");
            player.sendMessage("  §f●  §6§lCustom items §f🌷");

            // Clickable Discord link
            TextComponent discord = new TextComponent("  §f●  §9§lDiscord §b§l<CLICK ME>");
            discord.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/rdXkTHtGbW"));
            player.spigot().sendMessage(discord);

            // Clickable Store link
            TextComponent store = new TextComponent("  §f●  §a§lStore §2§l<CLICK ME>");
            store.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://godllybox.tebex.io"));
            player.spigot().sendMessage(store);

            player.sendMessage(" ");
            player.sendMessage("§x§0§F§B§4§F§B§m===============================");
        }, 40L); // 2 seconds delay (40 ticks)
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = plugin.getConfig();

        // Get configurable quit message
        String quitMessage = config.getString("messages.quit", "§7[§c←§7] §c%player%")
                .replace("%player%", player.getName());
        event.setQuitMessage(quitMessage);
    }
}
