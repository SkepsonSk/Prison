package pl.trollcraft;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.command.*;
import pl.trollcraft.command.warp.SetWarpCommand;
import pl.trollcraft.command.warp.WarpCommand;
import pl.trollcraft.listener.BlockBreakListener;
import pl.trollcraft.listener.InventoryListener;
import pl.trollcraft.listener.MinerListener;
import pl.trollcraft.listener.MoveListener;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.selling.SellCommand;
import pl.trollcraft.selling.SellingUtils;
import pl.trollcraft.util.*;
import pl.trollcraft.util.enchants.EnchantRegister;

import java.sql.SQLException;

public class Main extends JavaPlugin {

    private static Main instance;
    private static DatabaseHandler databaseHandler;
    private static YamlConfiguration sells;

    @Override
    public void onEnable() {
        instance = this;
        saveConfig();
        databaseHandler = new DatabaseHandler(this);
        databaseHandler.prepare();
        sells = Configs.load("selling.yml", this);
        Warp.load();
        DelayedWarp.listen();
        MoveDetect.listen();
        SellingUtils.calcPrices();
        EnchantRegister.register();
        GuiUtils.createInventory();

        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("helpop").setExecutor(new HelpopCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("sellall").setExecutor(new SellCommand());
        getCommand("blastpick").setExecutor(new PickaxeCommand());
        getCommand("guitest").setExecutor(new GuiCommand());

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
        getServer().getPluginManager().registerEvents(new MinerListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        //REMOVED MINE LISTENER OF SELLING PLUGIN
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
    public static YamlConfiguration getSells() { return sells; }
}
