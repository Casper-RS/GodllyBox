package dev.casperschotman.godllyCore.messages;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.ChatColor;

public class PrefixHandler {
    private static String prefix = "§8[§3ɢᴏᴅʟʟʏʙᴏx§8] §7≫ "; // Default prefix
    private static String tutPrefix = "§8[&6ᴛᴜᴛᴏʀɪᴀʟ&8]§7≫ ";
    private static GodllyCore plugin;

    // Initialize the PrefixHandler with plugin instance
    public static void init(GodllyCore corePlugin) {
        plugin = corePlugin;
        loadPrefix();
    }

    // Load the prefix from config.yml
    public static void loadPrefix() {
        if (plugin != null) {
            prefix = ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("prefix", prefix)); // Get from config, default if missing
        }
    }

    // Get the current prefix
    public static String getPrefix() {
        return prefix;
    }

    public static String getTutPrefix() {
        return tutPrefix;
    }
}
