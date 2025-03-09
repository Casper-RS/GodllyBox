package dev.casperschotman.godllyCore.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import dev.casperschotman.godllyCore.GodllyCore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static dev.casperschotman.godllyCore.messages.PrefixHandler.getTutPrefix;

public class TutorialCommand implements CommandExecutor, Listener {

	private final GodllyCore plugin;
	private final Map<UUID, Boolean> tutorialCompleted = new HashMap<>();
	private final Map<UUID, Boolean> inTutorial = new HashMap<>();
	private final Map<Player, Location> originalLocations = new HashMap<>();
	private final Map<Player, GameMode> originalGameModes = new HashMap<>();

	// Configurable values
	private int titleFadeIn;
	private int titleStay;
	private int titleFadeOut;
	private int chatStartDelay;
	private int messageInterval;

	public TutorialCommand(GodllyCore plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin); // Register event listener
		loadConfig(); // Load config values on startup
	}

	private void loadConfig() {
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();

		titleFadeIn = config.getInt("tutorial.title-fade-in", 10);
		titleStay = config.getInt("tutorial.title-stay", 40);
		titleFadeOut = config.getInt("tutorial.title-fade-out", 10);
		chatStartDelay = config.getInt("tutorial.chat-start-delay", 60);
		messageInterval = config.getInt("tutorial.message-interval", 70);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players can use this command!");
			return true;
		}

		Player player = (Player) sender;
		UUID playerUUID = player.getUniqueId();

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reset") && player.hasPermission("godllycore.tutorial.reset")) {
				tutorialCompleted.remove(playerUUID);
				player.sendMessage(ChatColor.GREEN + "Your tutorial state has been reset.");
				return true;
			} else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("godllycore.tutorial.reload")) {
				loadConfig();
				player.sendMessage(ChatColor.GREEN + "Tutorial settings reloaded from config!");
				return true;
			}
		}

		// Check if player has already completed the tutorial
		if (tutorialCompleted.getOrDefault(playerUUID, false)) {
			player.sendMessage(ChatColor.RED + "You have already completed the tutorial!");
			return true;
		}

		// Store original location and gamemode
		originalLocations.put(player, player.getLocation());
		originalGameModes.put(player, player.getGameMode());

		// Set player to spectator mode & mark them as "in tutorial"
		player.setGameMode(GameMode.SPECTATOR);
		inTutorial.put(playerUUID, true);

		player.sendMessage(ChatColor.GREEN + "Welcome to the tutorial! Follow along.");
		startTutorial(player);

		return true;
	}

	private void startTutorial(Player player) {
		UUID playerUUID = player.getUniqueId();

		Location[] tutorialLocations = {
				new Location(Bukkit.getWorld("s5"), 46.0, -31.5, 174, -39.0f, 20.0f),
				new Location(Bukkit.getWorld("s5"), 100, -44, 307.5, 180.0f, 33.0f),
				new Location(Bukkit.getWorld("s5"), 68.5, -57, 217.5, 153.5f, 9.0f)
		};

		String[] titles = {
				"§aWelcome to §3GodllyBox!",
				"§aSpawn Area",
				"§eGameplay"
		};

		String[] subtitles = {
				"§fLet's take a quick tour!",
				"§fThis is the main spawn.",
				"§fStart mining!"
		};

		String[][] chatMessages = {
				{
						getTutPrefix() + "§aWelcome §e" + player.getName() + " §7!",
						getTutPrefix() + "§aEnjoy the world of boxmining, featuring §e10 different dimensions§a!",
						getTutPrefix() + "§aExperience §dCustom Items, §4PvP, §ePrestiges, §cRebirths§a and many more!"
				},
				{
						getTutPrefix() + "§aThis is the main spawn, where all people meet!",
						getTutPrefix() + "§aOn your left side you will find §eranked mines§a.",
						getTutPrefix() + "§aOnly people with a buycraft rank can access it!",
						getTutPrefix() + "§aNext up, on the right side is the §eAFK area§a.",
						getTutPrefix() + "§aStand AFK to earn afk tokens which can be used for cool items!"
				},
				{
						getTutPrefix() + "§aOnce you are walking out of the main spawn,",
						getTutPrefix() + "§ayou will encounter various mines, in §edifferent worlds§a!",
						getTutPrefix() + "§aWith the starter tools you get after this tutorial,",
						getTutPrefix() + "§ayou can start mining at §8stone§7.",
						getTutPrefix() + "§aAt every mine you can find a shopkeeper.",
						getTutPrefix() + "§aThe shopkeeper is used to §eupgrade§7 every tool and get an §eautocompressor§7.",
						getTutPrefix() + "§aGood luck on your journey! Make sure to ask staff if you have any questions!"
				}
		};

		new BukkitRunnable() {
			int step = 0;

			@Override
			public void run() {
				if (step < tutorialLocations.length) {
					player.teleport(tutorialLocations[step]);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1f, 1f);
					player.sendTitle(titles[step], subtitles[step], titleFadeIn, titleStay, titleFadeOut);

					// Wait for chat messages to finish before teleporting to the next step
					sendChatMessages(player, chatMessages[step], () -> {
						step++;
						if (step >= tutorialLocations.length) {
							// End tutorial: teleport back and restore gamemode
							player.teleport(originalLocations.get(player));
							player.setGameMode(originalGameModes.get(player));

							player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1.15f);
							player.sendTitle(ChatColor.GREEN + "Tutorial Completed!", ChatColor.YELLOW + "Enjoy your adventure!", 10, 60, 10);
							player.sendMessage(ChatColor.LIGHT_PURPLE + "You have completed the tutorial!");

							originalLocations.remove(player);
							originalGameModes.remove(player);

							// Mark the tutorial as completed & remove from "in tutorial"
							tutorialCompleted.put(playerUUID, true);
							inTutorial.remove(playerUUID);

							cancel();
						} else {
							// Continue tutorial to next step
							this.run();
						}
					});
				}
			}
		}.runTaskLater(plugin, 20); // Delay to ensure smooth execution
	}

	private void sendChatMessages(Player player, String[] messages, Runnable onComplete) {
		new BukkitRunnable() {
			int index = 0;

			@Override
			public void run() {
				if (index < messages.length) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages[index]));
					index++;
				} else {
					cancel();
					if (onComplete != null) {
						onComplete.run(); // Callback to continue the tutorial only after messages are done
					}
				}
			}
		}.runTaskTimer(plugin, 0, messageInterval);
	}
}