package dev.casperschotman.godllyCore.managers;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class BossBarManager {
    private final GodllyCore plugin;
    private final List<BossBar> bossBars = new ArrayList<>();
    private int currentBossBarIndex = 0;
    private BukkitTask bossBarTask;

    public BossBarManager(GodllyCore plugin) {
        this.plugin = plugin;
        setupBossBars();
        startBossBarRotation();
    }

    private void setupBossBars() {
        bossBars.clear(); // Ensure no duplicates

        BossBar bar1 = Bukkit.createBossBar(
                ChatColor.translateAlternateColorCodes('&', "&6IP: &e&lgodllybox.minehut.gg"),
                BarColor.YELLOW,
                BarStyle.SOLID
        );
        bar1.setProgress(1.0);

        BossBar bar2 = Bukkit.createBossBar(
                ChatColor.translateAlternateColorCodes('&', "&3&lJoin our community: &9&l/discord"),
                BarColor.BLUE,
                BarStyle.SOLID
        );
        bar2.setProgress(1.0);

        BossBar bar3 = Bukkit.createBossBar(
                ChatColor.translateAlternateColorCodes('&', "&c&lMake sure to read the &4&l/rules"),
                BarColor.PURPLE,
                BarStyle.SOLID
        );
        bar3.setProgress(1.0);

        BossBar bar4 = Bukkit.createBossBar(
                ChatColor.translateAlternateColorCodes('&', "&a&lSupport us: &2&lgodllybox.tebex.io"),
                BarColor.GREEN,
                BarStyle.SOLID
        );
        bar4.setProgress(1.0);

        bossBars.add(bar1);
        bossBars.add(bar2);
        bossBars.add(bar3);
        bossBars.add(bar4);
    }

    public void startBossBarRotation() {
        if (bossBars.isEmpty()) return;

        stopBossBars(); // Ensure no duplicate tasks
        currentBossBarIndex = 0;

        bossBarTask = new BukkitRunnable() {
            @Override
            public void run() {
                removeAllBossBars(); // Remove previous boss bars

                BossBar currentBar = bossBars.get(currentBossBarIndex);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    currentBar.addPlayer(player);
                }

                currentBossBarIndex = (currentBossBarIndex + 1) % bossBars.size();
            }
        }.runTaskTimer(plugin, 0, 10 * 20L); // Default: rotates every 10 seconds
    }

    private void stopBossBars() {
        if (bossBarTask != null) {
            bossBarTask.cancel();
            bossBarTask = null;
            plugin.getLogger().info("[GodllyCore] Boss bar rotation stopped.");
        }
        removeAllBossBars();
    }

    private void removeAllBossBars() {
        for (BossBar bossBar : bossBars) {
            bossBar.removeAll();
        }
    }
}
