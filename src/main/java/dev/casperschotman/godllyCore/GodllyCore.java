package dev.casperschotman.godllyCore;

import dev.casperschotman.godllyCore.commands.*;
import dev.casperschotman.godllyCore.listeners.CommandListener;

import dev.casperschotman.godllyCore.listeners.ItemDropListener;
import dev.casperschotman.godllyCore.tabcompletion.CoreTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public final class GodllyCore extends JavaPlugin {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";


    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info(BLUE + "GodllyCore" + GREEN + " has been enabled!" + RESET);

        /////// --- FLY COMMAND --- ///////
        getCommand("fly").setExecutor(new FlyCommand());

        ///// -- GAMEMODE COMMANDS -- /////
        getCommand("gmc").setExecutor(new GameModeCommand());
        getCommand("gms").setExecutor(new GameModeCommand());
        getCommand("gmsp").setExecutor(new GameModeCommand());
        getCommand("gma").setExecutor(new GameModeCommand());

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

        ////// -- EVENT LISTENERS -- /////
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(dropCommand), this);  // Pass dropCommand to the listener

        /////// -- CORE COMMANDS -- ///////
        Core coreCommands = new Core(this);
        getCommand("core").setExecutor(coreCommands);
        getCommand("core").setTabCompleter(new CoreTabCompleter());

        /////// -- VANISH COMMANDS -- ///////
        getCommand("vanish").setExecutor(new VanishCommand(this));

    }

    @Override
    public void onDisable() {
        getLogger().info(BLUE + "GodllyCore" + RED + " has been disabled!" + RESET);
    }
}