package pl.trollcraft.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;

import java.util.HashMap;

public class MinersManager {

    private static HashMap<Player, Integer> blocksMined = new HashMap<>();

    public static void add(Player player, int blocks) {
        if (blocksMined.containsKey(player)){
            int mined = blocksMined.get(player);
            blocksMined.replace(player, mined + blocks);
        }
        else{
            blocksMined.put(player, blocks);
        }
    }

    public static int get(Player player){
        if (blocksMined.containsKey(player)) return blocksMined.get(player);
        return 0;
    }

    public static void save(Player player) {
        if (!blocksMined.containsKey(player)) return;
        Bukkit.getConsoleSender().sendMessage(get(player) + "");
        String name = player.getName();
        YamlConfiguration conf = Configs.load("miners.yml", Main.getInstance());
        conf.set("miners." + name + ".mined", blocksMined.get(player));
        blocksMined.remove(player);
        Configs.save(conf, "miners.yml");
    }

    public static void load(Player player) {
        String name = player.getName();
        YamlConfiguration conf = Configs.load("miners.yml", Main.getInstance());
        if (conf.contains("miners." + name))
            blocksMined.put(player, conf.getInt("miners." + name + ".mined"));
        else blocksMined.put(player, 0);
    }

}
