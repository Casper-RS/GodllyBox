package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class EssentialCommand implements CommandExecutor, TabCompleter, Listener {
    private final GodllyCore plugin;
    private final Map<UUID, Long> afkTimers = new HashMap<>();
    private final Set<UUID> afkPlayers = new HashSet<>();
    private final Set<UUID> godModePlayers = new HashSet<>();
    private final Map<UUID, Location> lastDeathLocation = new HashMap<>();
    private final Set<UUID> confirmClear = new HashSet<>();
    private final Map<UUID, Location> lastTeleportLocation = new HashMap<>();
    private final Set<UUID> spyingPlayers = new HashSet<>();

    public EssentialCommand(GodllyCore plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startAfkChecker();
    }

    public void registerCommands() {
        String[] commands = {"craft", "cspy", "anvil", "godmode", "back", "afk", "clear", "confirm", "enderchest", "item", "rename"};
        for (String cmd : commands) {
            if (plugin.getCommand(cmd) != null) {
                plugin.getCommand(cmd).setExecutor(this);
                plugin.getCommand(cmd).setTabCompleter(this);
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

            case "godmode":
                if (!player.hasPermission("godllycore.essentials.godmode")) return noPermission(player);
                if (godModePlayers.contains(player.getUniqueId())) {
                    godModePlayers.remove(player.getUniqueId());
                    player.sendMessage(getPrefix() + ChatColor.RED + "Godmode disabled.");
                } else {
                    godModePlayers.add(player.getUniqueId());
                    player.sendMessage(getPrefix() + ChatColor.GREEN + "Godmode enabled.");
                }
                break;

            case "back":
                if (!player.hasPermission("godllycore.essentials.back")) return noPermission(player);

                UUID playerId = player.getUniqueId();

                if (lastTeleportLocation.containsKey(playerId)) {
                    player.teleport(lastTeleportLocation.get(playerId));
                    player.sendMessage(getPrefix() + ChatColor.GREEN + "Teleported to your last location before teleporting.");
                    lastTeleportLocation.remove(playerId); // Remove after use to avoid reusing the same location
                } else if (lastDeathLocation.containsKey(playerId)) {
                    player.teleport(lastDeathLocation.get(playerId));
                    player.sendMessage(getPrefix() + ChatColor.GREEN + "Teleported to your last death location.");
                } else {
                    player.sendMessage(getPrefix() + ChatColor.RED + "No previous location recorded.");
                }
                break;

            case "cspy":
                if (!player.hasPermission("godllycore.essentials.cspy")) return noPermission(player);

                if (args.length == 0) { // Fix: If no arguments, show usage
                    player.sendMessage(getPrefix() + ChatColor.RED + "Usage: /cspy <on/off>");
                    return true;
                }

                String option = args[0].toLowerCase(); // Normalize input

                if (option.equals("on")) {
                    spyingPlayers.add(player.getUniqueId());
                    player.sendMessage(getPrefix() + ChatColor.GREEN + "Command Spy enabled.");
                } else if (option.equals("off")) {
                    spyingPlayers.remove(player.getUniqueId());
                    player.sendMessage(getPrefix() + ChatColor.RED + "Command Spy disabled.");
                } else {
                    player.sendMessage(getPrefix() + ChatColor.RED + "Usage: /cspy <on/off>");
                }
                break;


            case "afk":
                if (!player.hasPermission("godllycore.essentials.afk")) return noPermission(player);
                toggleAfk(player);
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
                            player.sendMessage(getPrefix() + ChatColor.RED + "Invalid amount. Must be between 1 and " + material.getMaxStackSize() + ".");
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

    @EventHandler
    public void onPlayerTeleport(org.bukkit.event.player.PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Store the player's last location before teleporting
        lastTeleportLocation.put(playerId, player.getLocation());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        lastDeathLocation.put(player.getUniqueId(), player.getLocation());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (godModePlayers.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(org.bukkit.event.player.PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String command = event.getMessage(); // Full command (e.g., "/spawn")

        for (UUID uuid : spyingPlayers) {
            if (uuid.equals(sender.getUniqueId())) {
                continue; // Skip logging the player's own command
            }

            Player spy = Bukkit.getPlayer(uuid);
            if (spy != null && spy.isOnline()) {
                spy.sendMessage("§8[§cCSpy§8]§4 " + sender.getName() + "§c: §7" + command);
            }
        }
    }



    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        afkTimers.put(player.getUniqueId(), System.currentTimeMillis());
        removeAfkIfNeeded(player);
    }

    @EventHandler
    public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        Player player = event.getPlayer();
        afkTimers.put(player.getUniqueId(), System.currentTimeMillis());
        removeAfkIfNeeded(player);
    }

    @EventHandler
    public void onPlayerChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        afkTimers.put(player.getUniqueId(), System.currentTimeMillis());
        removeAfkIfNeeded(player);
    }

    @EventHandler
    public void onPlayerBlockBreak(org.bukkit.event.block.BlockBreakEvent event) {
        Player player = event.getPlayer();
        afkTimers.put(player.getUniqueId(), System.currentTimeMillis());
        removeAfkIfNeeded(player);
    }


    private void removeAfkIfNeeded(Player player) {
        if (afkPlayers.contains(player.getUniqueId())) {
            afkPlayers.remove(player.getUniqueId());
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is no longer AFK.");
        }
    }


    private void startAfkChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long afkTime = plugin.getConfig().getLong("afk-time", 60) * 1000; // 180 seconden standaard

                for (UUID uuid : new HashSet<>(afkTimers.keySet())) { // Kopie om ConcurrentModificationException te voorkomen
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null || !player.isOnline()) continue;

                    long lastActive = afkTimers.getOrDefault(uuid, System.currentTimeMillis()); // Default voorkomt null
                    if (System.currentTimeMillis() - lastActive > afkTime && !afkPlayers.contains(uuid)) {
                        toggleAfk(player);
                    }
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }


    private void toggleAfk(Player player) {
        if (player == null || !player.isOnline()) return; // Voorkomt null-pointer errors

        if (afkPlayers.contains(player.getUniqueId())) {
            afkPlayers.remove(player.getUniqueId());
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is no longer AFK.");
        } else {
            afkPlayers.add(player.getUniqueId());
            Bukkit.broadcastMessage(getPrefix() + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " is now AFK.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();
        Player player = (Player) sender;

        String cmd = command.getName().toLowerCase();

        switch (cmd) {
            case "cspy":
                if (!player.hasPermission("godllycore.essentials.cspy")) return Collections.emptyList();
                if (args.length == 1) {
                    return Arrays.asList("on", "off").stream()
                            .filter(s -> s.startsWith(args[0].toLowerCase())) // Filter based on input
                            .collect(Collectors.toList());
                }
                break;

            case "item":
                if (!player.hasPermission("godllycore.essentials.item")) return Collections.emptyList();
                if (args.length == 1) {
                    return Arrays.stream(Material.values())
                            .map(Material::name)
                            .map(String::toLowerCase)
                            .filter(name -> name.startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                }
                if (args.length == 2) {
                    return Arrays.asList("1", "16", "32", "64");
                }
                break;

            case "enderchest":
                if (!player.hasPermission("godllycore.essentials.enderchest")) return Collections.emptyList();
                if (args.length == 1) {
                    return Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                            .collect(Collectors.toList());
                }
                break;
        }
        return Collections.emptyList();
    }

}