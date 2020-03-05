package pl.trollcraft.obj;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.Debug;

import java.util.ArrayList;
import java.util.HashMap;

public class PrisonBlock {

    private static int counter = 0;
    private static PrisonBlock defaultBlock;
    private static ArrayList<PrisonBlock> blocks = new ArrayList<>();
    private static HashMap<Player, PrisonBlock> playerBlocks = new HashMap<>();

    // Also warp name
    private int value;
    private String name;
    private String next;
    private double enterPrice;
    private int enterBlocksMined;

    public PrisonBlock(String name, String next, double enterPrice, int enterBlocksMined) {
        value = counter;
        this.name = name;
        this.next = next;
        this.enterPrice = enterPrice;
        this.enterBlocksMined = enterBlocksMined;
        counter++;
        blocks.add(this);
    }

    public static ArrayList<PrisonBlock> getBlocks() { return blocks; }

    public String getName() { return name; }
    public boolean hasUnlocked(Player player) { return (playerBlocks.get(player).value >= value); }
    public PrisonBlock getNext() { return next != null ? get(next) : null; }
    public double getEnterPrice() { return enterPrice; }
    public int getEnterBlocksMined() { return enterBlocksMined; }

    // -------- -------- -------- -------- --------

    public static PrisonBlock get(String name) {
        for (PrisonBlock pb : blocks) {
            if (pb.name.equals(name)) return pb;
        }
        return null;
    }

    public static PrisonBlock getPlayerBlock(Player player) {
        if (!playerBlocks.containsKey(player)) return null;
        return playerBlocks.get(player);
    }

    // -------- -------- -------- -------- --------

    public static void unlock(Player player, PrisonBlock block) {
        playerBlocks.replace(player, block);
    }

    public static void assignToPlayer(Player player) {

        if (playerBlocks.containsKey(player)) return;

        YamlConfiguration conf = Configs.load("playerblocks.yml", Main.getInstance());
        if (conf.contains("playerblocks." + player.getName()))
            playerBlocks.put(player, get(conf.getString("playerblocks." + player.getName() + ".block")));
        else
            playerBlocks.put(player, defaultBlock);
    }

    public static void load() {
        YamlConfiguration conf = Configs.load("blocks.yml", Main.getInstance());
        conf.getConfigurationSection("blocks").getKeys(false).forEach( name -> {

            String next = null;
            if (conf.contains("blocks." + name + ".next"))
                next = conf.getString("blocks." + name + ".next");

            double enterPrice = conf.getDouble("blocks." + name + ".enterPrice");
            int enterBlocksMined = 0;

            if (conf.contains("blocks." + name + ".enterBlocksMined"))
                enterBlocksMined = conf.getInt("blocks." + name + ".enterBlocksMined");

            new PrisonBlock(name, next, enterPrice, enterBlocksMined);
        } );
        defaultBlock = get(conf.getString("defaultBlock"));
    }

    public static void savePlayer(Player player) {
        PrisonBlock pb = getPlayerBlock(player);
        if (pb == null) return;

        YamlConfiguration conf = Configs.load("playerblocks.yml", Main.getInstance());
        conf.set("playerblocks." + player.getName() + ".block", pb.name);

        Configs.save(conf, "playerblocks.yml");
    }

}
