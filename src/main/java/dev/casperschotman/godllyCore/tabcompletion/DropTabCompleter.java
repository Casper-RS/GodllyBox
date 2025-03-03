package dev.casperschotman.godllyCore.tabcompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DropTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> suggestions = new ArrayList<>();

		// If the sender is a player
		if (sender instanceof Player player) {
			// If the command is "/drop"
			if (args.length == 1) {
				// Add "toggle" as a possible completion
				suggestions.add("toggle");
			}
		}

		return suggestions;
	}
}
