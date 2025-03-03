package dev.casperschotman.godllyCore.tabcompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FlyTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();

		if (sender instanceof Player player) {
			if (args.length == 1) {
				suggestions.add("speed");
			}
			if (args.length == 2 && args[0].equalsIgnoreCase("speed")) {
				suggestions.add("reset");
				suggestions.add("0.1");
				suggestions.add("0.5");
				suggestions.add("1.0");
				suggestions.add("2.0");
				suggestions.add("5.0");
			}
		}

		return suggestions;
	}
}
