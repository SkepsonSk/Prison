package pl.trollcraft.obj;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;

import java.util.ArrayList;

public class Warp {

    private static ArrayList<Warp> warps = new ArrayList<Warp>();

    private String name;
    private Location location;
    private boolean locked;

    public Warp(String name, Location location, boolean locked) {
        this.name = name;
        this.location = location;
        this.locked = locked;
        warps.add(this);
    }

    public boolean isLocked() { return locked; }

    public void teleport(Player player) {
        if (!location.getChunk().isLoaded()) location.getChunk().load();
        player.teleport(location);
    }

    public void save() {
        YamlConfiguration conf = Configs.load("warps.yml", Main.getInstance());
        conf.set("warps." + name + ".loc", ChatUtil.locToString(location));
        conf.set("warps." + name + ".locked", locked);
        Configs.save(conf, "warps.yml");
    }

    public static Warp get(String name) {
        for (Warp w : warps){
            if (w.name.equals(name)) return w;
        }
        return null;
    }

    public static void load() {
        YamlConfiguration conf = Configs.load("warps.yml", Main.getInstance());
        conf.getConfigurationSection("warps").getKeys(false).forEach( name -> {
            Location loc = ChatUtil.locFromString(conf.getString("warps." + name + ".loc"));
            boolean locked = conf.getBoolean(conf.getString("warps." + name + ".locked"));
            new Warp(name, loc, locked);
        } );
    }

}
