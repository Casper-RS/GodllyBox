package dev.casperschotman.godllyCore;

import dev.casperschotman.godllyCore.commands.*;
import dev.casperschotman.godllyCore.listeners.*;
import dev.casperschotman.godllyCore.managers.BossBarManager;
import dev.casperschotman.godllyCore.messages.PrefixHandler;
import dev.casperschotman.godllyCore.tabcompletion.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class GodllyCore extends JavaPlugin {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";

    private AfkListener afkListener;
    private TeleportListener teleportListener;
    private GodmodeListener godmodeListener;
    private CommandSpyListener commandSpyListener;
    private DropCommand dropCommand;
    private BossBarManager bossBarManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        PrefixHandler.init(this); // Initialize prefix system

        getLogger().info("===================================");
        getLogger().info("||                               ||");
        getLogger().info("|| GodllyCore made by ItzRepsac_ ||");
        getLogger().info(GREEN + "|| GodllyCore has been enabled!  ||" + RESET);
        getLogger().info("||                               ||");
        getLogger().info("===================================");

        // Initialize Managers
        bossBarManager = new BossBarManager(this);
        bossBarManager.startBossBarRotation();

        // Initialize Listeners & Commands (store them in fields)
        dropCommand = new DropCommand(this);
        afkListener = new AfkListener(this);
        teleportListener = new TeleportListener(this);
        godmodeListener = new GodmodeListener(this);
        commandSpyListener = new CommandSpyListener(this);

        registerListeners();
        registerCommands();

        getLogger().info(GREEN + "All commands and listeners have been registered successfully!" + RESET);
    }

    @Override
    public void onDisable() {
        getLogger().info(BLUE + "GodllyCore" + RED + " has been disabled!" + RESET);
    }

    private void registerListeners() {
        List<Object> listeners = Arrays.asList(
                new CommandListener(),
                new ItemDropListener(dropCommand),
                new FullInventoryListener(this),
                new CustomDeathMessageListener(this),
                new JoinQuitListener(this),
                new FirstJoinListener(this),
                new PlayerRespawnListener(this),
                afkListener,
                teleportListener,
                godmodeListener,
                commandSpyListener
        );

        for (Object listener : listeners) {
            Bukkit.getPluginManager().registerEvents((org.bukkit.event.Listener) listener, this);
        }
    }

    private void registerCommands() {
        // Essential Commands
        EssentialCommand essentialsCommands = new EssentialCommand(this);
        essentialsCommands.registerCommands();

        // Register Commands with Tab Completion
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("fly").setTabCompleter(new FlyTabCompleter());

        GameModeCommand gameModeCommand = new GameModeCommand();
        GameModeTabCompleter gameModeTabCompleter = new GameModeTabCompleter();
        getCommand("gmc").setExecutor(gameModeCommand);
        getCommand("gms").setExecutor(gameModeCommand);
        getCommand("gmsp").setExecutor(gameModeCommand);
        getCommand("gma").setExecutor(gameModeCommand);
        getCommand("gmc").setTabCompleter(gameModeTabCompleter);
        getCommand("gms").setTabCompleter(gameModeTabCompleter);
        getCommand("gmsp").setTabCompleter(gameModeTabCompleter);
        getCommand("gma").setTabCompleter(gameModeTabCompleter);

        TeleportCommand teleportCommand = new TeleportCommand();
        getCommand("tp").setExecutor(teleportCommand);
        getCommand("tp").setTabCompleter(teleportCommand);
        getCommand("tphere").setExecutor(teleportCommand);
        getCommand("tphere").setTabCompleter(teleportCommand);
        getCommand("tpall").setExecutor(teleportCommand);
        getCommand("tpa").setExecutor(teleportCommand);
        getCommand("tpa").setTabCompleter(teleportCommand);
        getCommand("tpaccept").setExecutor(teleportCommand);
        getCommand("tpadeny").setExecutor(teleportCommand);

        getCommand("drop").setExecutor(dropCommand);
        getCommand("drop").setTabCompleter(new DropTabCompleter());

        getCommand("core").setExecutor(new Core(this, new FullInventoryListener(this)));
        getCommand("core").setTabCompleter(new CoreTabCompleter());

        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("toggleinvfull").setExecutor(new InvFullToggleCommand(this));
        getCommand("discord").setExecutor(new DiscordCommand());
        getCommand("spawn").setExecutor(new SpawnCommand(this));

        getCommand("cspy").setExecutor(essentialsCommands);
        getCommand("cspy").setTabCompleter(new EssentialTabCompleter());
        getCommand("item").setTabCompleter(new EssentialTabCompleter());
        getCommand("enderchest").setTabCompleter(new EssentialTabCompleter());
    }

    // Getter methods to retrieve initialized instances
    public AfkListener getAfkListener() {
        return afkListener;
    }

    public TeleportListener getTeleportListener(){
        return teleportListener;
    }

    public GodmodeListener getGodModeListener(){
        return godmodeListener;
    }

    public CommandSpyListener getCommandSpyListener(){
        return commandSpyListener;
    }

    public DropCommand getDropCommand() {
        return dropCommand;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }
}
