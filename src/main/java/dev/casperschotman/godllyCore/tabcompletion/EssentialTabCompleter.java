package dev.casperschotman.godllyCore.tabcompletion;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class EssentialTabCompleter implements TabCompleter {

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
                            .filter(s -> s.startsWith(args[0].toLowerCase()))
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
