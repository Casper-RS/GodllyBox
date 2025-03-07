package dev.casperschotman.godllyCore;

import dev.casperschotman.godllyCore.commands.*;
import dev.casperschotman.godllyCore.listeners.*;
import dev.casperschotman.godllyCore.tabcompletion.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class GodllyCore extends JavaPlugin {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("===================================");
        getLogger().info("||                               ||");
        getLogger().info("|| GodllyCore made by ItzRepsac_ ||");
        getLogger().info(GREEN + "|| GodllyCore has been enabled!  ||" + RESET);
        getLogger().info("||                               ||");
        getLogger().info("===================================");

        /////// --- FLY COMMAND --- ///////
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("fly").setTabCompleter(new FlyTabCompleter());

        ///// -- GAMEMODE COMMANDS -- /////
        GameModeCommand GameMode = new GameModeCommand();
        GameModeTabCompleter GameModeTabCompletion = new GameModeTabCompleter();
        getCommand("gmc").setExecutor(GameMode);
        getCommand("gms").setExecutor(GameMode);
        getCommand("gmsp").setExecutor(GameMode);
        getCommand("gma").setExecutor(GameMode);
        getCommand("gmc").setTabCompleter(GameModeTabCompletion);
        getCommand("gms").setTabCompleter(GameModeTabCompletion);
        getCommand("gmsp").setTabCompleter(GameModeTabCompletion);
        getCommand("gma").setTabCompleter(GameModeTabCompletion);

        ///// -- TELEPORT COMMANDS -- /////
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

        /////// -- DROP COMMAND -- ///////
        DropCommand dropCommand = new DropCommand(this);
        getCommand("drop").setExecutor(dropCommand);
        getCommand("drop").setTabCompleter(new DropTabCompleter());

        // Register command executor and tab completer
        EssentialCommand essentialsCommands = new EssentialCommand(this);
        essentialsCommands.registerCommands();

        ////// -- EVENT LISTENERS -- /////
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(dropCommand), this);
        getServer().getPluginManager().registerEvents(new FullInventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomDeathMessageListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(this), this);
        getServer().getPluginManager().registerEvents(essentialsCommands, this);

        /////// -- CORE COMMANDS -- ///////
        getCommand("core").setExecutor(new Core(this, new FullInventoryListener(this)));
        getCommand("core").setTabCompleter(new CoreTabCompleter());


        /////// -- VANISH COMMANDS -- ///////
        getCommand("vanish").setExecutor(new VanishCommand(this));

        ////// -- FULL INV COMMAND -- //////
        getCommand("toggleinvfull").setExecutor(new InvFullToggleCommand(this));

        /////// -- DISCORD COMMAND -- //////
        getCommand("discord").setExecutor(new DiscordCommand());

        //////// -- SPAWN COMMAND -- ///////
        getCommand("spawn").setExecutor(new SpawnCommand(this)); // Register spawn command
        }

    @Override
    public void onDisable() {
        getLogger().info(BLUE + "GodllyCore" + RED + " has been disabled!" + RESET);
    }
}