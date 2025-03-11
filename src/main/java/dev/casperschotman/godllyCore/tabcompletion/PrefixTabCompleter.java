package dev.casperschotman.godllyCore.tabcompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefixTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!(sender instanceof Player player)) return null;
		if (!player.hasPermission("prefix.custom")) return null;

		List<String> completions = new ArrayList<>();

		if (args.length == 1) {
			completions.addAll(Arrays.asList("set", "remove"));
		} else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
			completions.add("<your-prefix>"); // Placeholder for player to type their prefix
		}

		return completions;
	}
}
