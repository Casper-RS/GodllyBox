package dev.casperschotman.godllyCore.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

public class AdvertiseCommand implements CommandExecutor {

	private final String rankCommand = "/ad godllybox &9&LCUSTOM ITEMS &f| &c&lPRESTIGES  &f| &d&lKIND COMMUNITY";
	private final String noRankCommand = "/ad godllybox CUSTOM ITEMS | PRESTIGES  | KIND COMMUNITY";

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage(getPrefix() + ChatColor.RED + "Only players can use this command!");
			return true;
		}

		TextComponent rankMessage = new TextComponent(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Click to copy ad message!");
		rankMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, rankCommand));
		player.spigot().sendMessage(rankMessage);

		TextComponent noRankMessage = new TextComponent(ChatColor.YELLOW + "" + ChatColor.UNDERLINE + "Click to copy ad message!");
		noRankMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, noRankCommand));
		player.spigot().sendMessage(noRankMessage);

		return true;
	}
}
