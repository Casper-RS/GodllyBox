package dev.casperschotman.godllyCore.tabcompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CoreTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        // If this is the first argument (after /core)
        if (args.length == 1) {
            // Add "reload" as a possible subcommand
            completions.add("reload");
            completions.add("info");
        }

        return completions;
    }
}
