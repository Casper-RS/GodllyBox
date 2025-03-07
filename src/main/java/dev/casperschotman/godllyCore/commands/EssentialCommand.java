package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import dev.casperschotman.godllyCore.listeners.AfkListener;
import dev.casperschotman.godllyCore.listeners.CommandSpyListener;
import dev.casperschotman.godllyCore.listeners.GodmodeListener;
import dev.casperschotman.godllyCore.listeners.TeleportListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class EssentialCommand implements CommandExecutor, Listener {
    private final GodllyCore plugin;
    private final Set<UUID> confirmClear = new HashSet<>();

    public EssentialCommand(GodllyCore plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void registerCommands() {
        String[] commands = {"craft", "cspy", "anvil", "godmode", "back", "afk", "clear", "confirm", "enderchest", "item", "rename"};
        for (String cmd : commands) {
            if (plugin.getCommand(cmd) != null) {
                plugin.getCommand(cmd).setExecutor(this);
            } else {
                plugin.getLogger().warning("Command /" + cmd + " is not defined in plugin.yml!");
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        String cmd = command.getName().toLowerCase();

        switch (cmd) {
            case "craft":
                if (!player.hasPermission("godllycore.essentials.craft")) return noPermission(player);
                player.openWorkbench(null, true);
                break;

            case "anvil":
                if (!player.hasPermission("godllycore.essentials.anvil")) return noPermission(player);
                Inventory anvil = Bukkit.createInventory(null, InventoryType.ANVIL);
                player.openInventory(anvil);
                break;

            case "back":
                if (!player.hasPermission("godllycore.essentials.back")) return noPermission(player);

                TeleportListener backListener = plugin.getTeleportListener();
                if (backListener == null) {
                    player.sendMessage(ChatColor.RED + "Back system is not initialized.");
                } else {
                    backListener.teleportBack(player);
                }
                break;

            case "godmode":
                if (!player.hasPermission("godllycore.essentials.godmode")) return noPermission(player);

                GodmodeListener godModeListener = plugin.getGodModeListener();
                if (godModeListener == null) {
                    player.sendMessage(ChatColor.RED + "God Mode system is not initialized.");
                } else {
                    godModeListener.toggleGodMode(player);
                }
                break;

            case "cspy":
                if (!player.hasPermission("godllycore.essentials.cspy")) return noPermission(player);

                CommandSpyListener commandSpyListener = plugin.getCommandSpyListener();
                if (commandSpyListener == null) {
                    player.sendMessage(ChatColor.RED + "Command Spy system is not initialized.");
                } else {
                    commandSpyListener.toggleCommandSpy(player);
                }
                break;

            case "afk":
                if (!player.hasPermission("godllycore.essentials.afk")) return noPermission(player);

                AfkListener afkListener = plugin.getAfkListener();
                if (afkListener == null) {
                    player.sendMessage(ChatColor.RED + "AFK system is not initialized.");
                } else {
                    afkListener.toggleAfk(player);
                }
                break;


            case "clear":
                if (!player.hasPermission("godllycore.essentials.clear")) return noPermission(player);
                confirmClear.add(player.getUniqueId());
                player.sendMessage(getPrefix() + ChatColor.YELLOW + "Are you sure? Type /confirm to clear your inventory.");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (confirmClear.contains(player.getUniqueId())) {
                            confirmClear.remove(player.getUniqueId());
                            player.sendMessage(getPrefix() + ChatColor.RED + "Your clear request has expired. Type /clear again.");
                        }
                    }
                }.runTaskLater(plugin, 200);
                break;

            case "confirm":
                if (confirmClear.contains(player.getUniqueId())) {
                    player.getInventory().clear();
                    player.sendMessage(getPrefix() + ChatColor.GREEN + "Your inventory has been cleared.");
                    confirmClear.remove(player.getUniqueId());
                }
                break;

            case "enderchest":
                if (!player.hasPermission("godllycore.essentials.enderchest")) return noPermission(player);
                if (args.length > 1 && player.hasPermission("godllycore.essentials.enderchest.others")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        player.openInventory(target.getEnderChest());
                        player.sendMessage(getPrefix() + ChatColor.GREEN + "Opened " + target.getName() + "'s Ender Chest.");
                    } else {
                        player.sendMessage(getPrefix() + ChatColor.RED + "Player not found.");
                    }
                } else {
                    player.openInventory(player.getEnderChest());
                }
                break;

            case "item":
                if (!player.hasPermission("godllycore.essentials.item")) return noPermission(player);

                if (args.length < 1) { // Should be args.length < 1 instead of args.length < 2
                    player.sendMessage(getPrefix() + ChatColor.RED + "Usage: /item <material> [amount]");
                    return true;
                }

                Material material = Material.matchMaterial(args[0]); // Change args[1] to args[0]
                if (material == null) {
                    player.sendMessage(getPrefix() + ChatColor.RED + "Invalid material.");
                    return true;
                }

                int amount = 1; // Default amount is 1
                if (args.length >= 2) { // Change from args.length >= 3 to args.length >= 2
                    try {
                        amount = Integer.parseInt(args[1]); // Change args[2] to args[1]
                        if (amount < 1 || amount > material.getMaxStackSize()) {
                            player.sendMessage(getPrefix() + ChatColor.RED + "Unstackable item. Must be between 1 and " + material.getMaxStackSize() + ".");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(getPrefix() + ChatColor.RED + "Invalid number for amount.");
                        return true;
                    }
                }

                player.getInventory().addItem(new ItemStack(material, amount));
                player.sendMessage(getPrefix() + ChatColor.GREEN + "Given " + amount + "x " + material.name().toLowerCase().replace("_", " "));
                break;


            case "rename":
                if (!player.hasPermission("godllycore.essentials.rename")) return noPermission(player);
                if (args.length < 1) { // Change from args.length < 2 to args.length < 1
                    player.sendMessage(getPrefix() + ChatColor.RED + "Usage: /rename <new name>");
                    return true;
                }
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(getPrefix() + ChatColor.RED + "You must be holding an item to rename it.");
                    return true;
                }
                ItemMeta meta = item.getItemMeta();
                if (meta == null) {
                    player.sendMessage(getPrefix() + ChatColor.RED + "This item cannot be renamed.");
                    return true;
                }

                // Fix indexing: Use args[0] instead of args[1] and update the join range
                String newName = ChatColor.translateAlternateColorCodes('&', String.join(" ", args));
                meta.setDisplayName(newName);
                item.setItemMeta(meta);
                player.sendMessage(getPrefix() + ChatColor.GREEN + "Renamed item to: " + newName);
                break;

            default:
                player.sendMessage(getPrefix() + ChatColor.RED + "Unknown subcommand.");
        }
        return true;
    }

    private boolean noPermission(Player player) {
        player.sendMessage(getPrefix() + ChatColor.RED + "You don't have permission to use this command.");
        return true;
    }
}