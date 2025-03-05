package dev.casperschotman.godllyCore.commands;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getPrefix;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {

            // If no arguments are provided, toggle fly mode
            if (args.length == 0) {
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    player.sendMessage(getPrefix() + "§cFly mode disabled.");
                } else {
                    player.setAllowFlight(true);
                    player.sendMessage(getPrefix() + "§aFly mode enabled.");
                }
                return true;
            }

            // If 'speed' subcommand is provided
            if (args.length == 2 && args[0].equalsIgnoreCase("speed")) {
                // Check if the "reset" argument is passed
                if (args[1].equalsIgnoreCase("reset")) {
                    // Reset the fly speed to the default value (e.g., 0.1)
                    player.setFlySpeed(0.1f);  // The default fly speed is 0.1
                    player.sendMessage(getPrefix() + "§aFly speed has been reset to the default.");
                    return true;
                }

                // Try to parse the speed argument
                try {
                    float speed = Float.parseFloat(args[1]);

                    // Validate the speed value (set a range from 0.1 to 10)
                    if (speed < 0.1 || speed > 5) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                        player.sendMessage(getPrefix() + "§cInvalid speed value. Please choose a value between 0.1 and 5.");
                        return false;
                    }

                    // Set the fly speed
                    player.setFlySpeed(speed / 10);  // Fly speed range is between 0.0 and 1.0, so we divide by 10
                    player.sendMessage(getPrefix() + "§aFly speed set to " + speed + "!");
                    return true;

                } catch (NumberFormatException e) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    player.sendMessage(getPrefix() + "§cInvalid number format for speed. Please enter a valid number.");
                    return false;
                }
            }

            // If the argument is unknown
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            player.sendMessage(getPrefix() + "§cUnknown subcommand. Usage: /fly [speed <value> | speed reset]");
            return false;

        } else {
            sender.sendMessage(getPrefix() + "§cThis command can only be used by a player.");
        }
        return true;
    }
}
