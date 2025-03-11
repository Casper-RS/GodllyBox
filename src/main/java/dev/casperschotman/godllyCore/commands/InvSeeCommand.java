package dev.casperschotman.godllyCore.commands;

import dev.casperschotman.godllyCore.GodllyCore;
import dev.casperschotman.godllyCore.messages.PrefixHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvSeeCommand implements CommandExecutor {

    private final GodllyCore plugin;

    public InvSeeCommand(GodllyCore plugin) {
        this.plugin = plugin;
        plugin.getCommand("invsee").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command!");
            return true;
        }

        if (!player.hasPermission("godllycore.invsee.view")) {
            player.sendMessage(PrefixHandler.getPrefix() + ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(PrefixHandler.getPrefix() + ChatColor.YELLOW + "Usage: /invsee <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(PrefixHandler.getPrefix() + ChatColor.RED + "Player not found or offline.");
            return true;
        }

        boolean canEdit = player.hasPermission("godllycore.invsee.edit");

        Inventory invseeInventory = Bukkit.createInventory(null, 45, ChatColor.AQUA + target.getName() + "'s Inventory");

        // Copy target's inventory
        ItemStack[] targetContents = target.getInventory().getContents();
        ItemStack[] targetArmor = target.getInventory().getArmorContents();

        for (int i = 0; i < 36; i++) {
            invseeInventory.setItem(i, targetContents[i]);
        }

        // Place armor slots in the last four inventory slots
        for (int i = 0; i < targetArmor.length; i++) {
            invseeInventory.setItem(36 + i, targetArmor[i]);
        }

        player.openInventory(invseeInventory);

        if (canEdit) {
            player.sendMessage(PrefixHandler.getPrefix() + ChatColor.GREEN + "You can edit " + target.getName() + "'s inventory.");
        } else {
            player.sendMessage(PrefixHandler.getPrefix() + ChatColor.YELLOW + "You can only view " + target.getName() + "'s inventory.");
        }

        return true;
    }
}
