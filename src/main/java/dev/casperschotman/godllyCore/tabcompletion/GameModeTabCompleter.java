package dev.casperschotman.godllyCore.tabcompletion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameModeTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 1 && sender.hasPermission("godllycore.gamemode.others")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				completions.add(player.getName());
			}
		}

		return completions;
	}
}
