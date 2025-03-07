package dev.casperschotman.godllyCore.managers;

import org.bukkit.boss.BarColor;

public class BossBarConfig {
    String title;
    BarColor color;
    double progress;
    int duration;

    public static BossBarConfig fromMap(java.util.Map<?, ?> map) {
        String title = (String) map.get("title");
        BarColor color = BarColor.valueOf(((String) map.get("color")).toUpperCase());
        double progress = ((Number) map.get("progress")).doubleValue();
        int duration = ((Number) map.get("duration")).intValue();
        return new BossBarConfig(title, color, progress, duration);
    }

    public BossBarConfig(String title, BarColor color, double progress, int duration) {
        this.title = title;
        this.color = color;
        this.progress = progress;
        this.duration = duration;
    }
}
