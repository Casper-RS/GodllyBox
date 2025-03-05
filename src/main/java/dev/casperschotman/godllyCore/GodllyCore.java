package dev.casperschotman.godllyCore;

import dev.casperschotman.godllyCore.commands.*;
import dev.casperschotman.godllyCore.listeners.*;
import dev.casperschotman.godllyCore.tabcompletion.*;

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

    public FullInventoryListener fullInventoryListener;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("GodllyCore made by ItzRepsac_");
        getLogger().info(BLUE + "GodllyCore" + GREEN + " has been enabled!" + RESET);

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

        ////// -- EVENT LISTENERS -- /////
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new ItemDropListener(dropCommand), this);  // Pass dropCommand to the listener
        fullInventoryListener = new FullInventoryListener(this);
        getServer().getPluginManager().registerEvents(fullInventoryListener, this);

        /////// -- CORE COMMANDS -- ///////
        getCommand("core").setExecutor(new Core(this, fullInventoryListener));
        getCommand("core").setTabCompleter(new CoreTabCompleter());


        /////// -- VANISH COMMANDS -- ///////
        getCommand("vanish").setExecutor(new VanishCommand(this));

        ////// -- FULL INV COMMAND -- //////
        getCommand("toggleinvfull").setExecutor(new InvFullToggleCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info(BLUE + "GodllyCore" + RED + " has been disabled!" + RESET);
    }
}