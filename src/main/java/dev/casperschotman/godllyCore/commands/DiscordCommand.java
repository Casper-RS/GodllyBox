package dev.casperschotman.godllyCore.commands;
import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(getPrefix() + "§cOnly players can use this command.");
            return true;
        }

        // Regular messages
        player.sendMessage("§x§0§F§B§4§F§B§m                                                §x§7§2§0§2§F§F");
        player.sendMessage("              §x§0§F§B§4§F§BJoin the Discord!§x§7§2§0§2§F§F");


        TextComponent link = new TextComponent("                  §3§nCLICK Here!");
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/rdXkTHtGbW"));
        player.spigot().sendMessage(link);


        player.sendMessage("§x§0§F§B§4§F§B§m                                                §x§7§2§0§2§F§F");

        return true;
    }
}
