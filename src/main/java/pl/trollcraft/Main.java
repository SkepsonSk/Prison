package pl.trollcraft;

import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.command.FlyCommand;
import pl.trollcraft.command.GamemodeCommand;
import pl.trollcraft.command.HelpopCommand;
import pl.trollcraft.command.SpawnCommand;
import pl.trollcraft.command.warp.SetWarpCommand;
import pl.trollcraft.command.warp.WarpCommand;
import pl.trollcraft.listener.BlockBreakListener;
import pl.trollcraft.listener.MoveListener;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.util.DatabaseHandler;
import pl.trollcraft.util.DelayedWarp;
import pl.trollcraft.util.MoveDetect;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    private static Main instance;
    private static DatabaseHandler databaseHandler;

    @Override
    public void onEnable() {
        instance = this;
        saveConfig();
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.prepare();
        Warp.load();
        DelayedWarp.listen();
        MoveDetect.listen();

        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("helpop").setExecutor(new HelpopCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
    }

    @Override
    public void onDisable() {
        try {
            databaseHandler.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() { return instance; }
    public static DatabaseHandler getDatabaseHandler() { return databaseHandler; }
}
