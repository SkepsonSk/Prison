package pl.trollcraft;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.trollcraft.cells.VoidWorldChunkGenerator;
import pl.trollcraft.command.*;
import pl.trollcraft.command.cells.AccpetVisitCommand;
import pl.trollcraft.command.cells.CellCommand;
import pl.trollcraft.command.cells.VisitCellCommand;
import pl.trollcraft.command.envoy.EnvoyAdminCommand;
import pl.trollcraft.command.envoy.EnvoyCommand;
import pl.trollcraft.command.warp.SetWarpCommand;
import pl.trollcraft.command.warp.WarpCommand;
import pl.trollcraft.envoy.EnvoyChest;
import pl.trollcraft.listener.*;
import pl.trollcraft.obj.PrisonBlock;
import pl.trollcraft.obj.cells.CellNode;
import pl.trollcraft.obj.cells.PendingVisit;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.obj.enchanting.EnchantData;
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
        CellNode.load();
        PendingVisit.startTimer();
        EnchantData.load();
        PrisonBlock.load();
        EnvoyChest.ENVOY_WORLD = Bukkit.getWorld("envoy");
        EnvoyChest.loadEnvoyChests();
        EnvoyChest.loadEnvoyItems();
        EnvoyChest.init();

        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("helpop").setExecutor(new HelpopCommand());
        getCommand("setwarp").setExecutor(new SetWarpCommand());
        getCommand("warp").setExecutor(new WarpCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("sellall").setExecutor(new SellCommand());
        getCommand("blastpick").setExecutor(new PickaxeCommand());
        getCommand("autosell").setExecutor(new AutoSellCommand());
        getCommand("blasttest").setExecutor(new BlastCommand());
        getCommand("enchant").setExecutor(new EnchantCommand());
        getCommand("cell").setExecutor(new CellCommand());
        getCommand("visit").setExecutor(new VisitCellCommand());
        getCommand("accept").setExecutor(new AccpetVisitCommand());
        getCommand("promote").setExecutor(new PromoteCommand());
        getCommand("envoyadmin").setExecutor(new EnvoyAdminCommand());
        getCommand("envoy").setExecutor(new EnvoyCommand());
        getCommand("debugcells").setExecutor(new CellDebugCommand());

        getServer().getPluginManager().registerEvents(new MoveListener(), this);
        getServer().getPluginManager().registerEvents(new MinerListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        //REMOVED MINE LISTENER OF SELLING PLUGIN

        if (!VoidWorldChunkGenerator.exists())
            VoidWorldChunkGenerator.generate();

        if (!VoidWorldChunkGenerator.envoyExists())
            VoidWorldChunkGenerator.generateEnvoy();
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
