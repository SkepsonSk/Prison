package pl.trollcraft.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import sun.security.krb5.Config;

import java.util.HashMap;

public class AutoSell {

    public enum Operation { ENABLED, DISABLED }

    private static HashMap<Integer, Boolean> haveAutoSell = new HashMap<>();

    public static boolean hasEnabled(Player player) {
        int id = player.getEntityId();
        if (haveAutoSell.containsKey(id))
            return haveAutoSell.get(id);
        return false;
    }

    public static AutoSell.Operation change(Player player) {
        int id = player.getEntityId();
        if (haveAutoSell.containsKey(id)){
            if (haveAutoSell.get(id)){
                haveAutoSell.replace(id, false);
                return AutoSell.Operation.DISABLED;
            }
            else{
                haveAutoSell.replace(id, true);
                return AutoSell.Operation.ENABLED;
            }
        }
        else{
            haveAutoSell.put(id, true);
            return AutoSell.Operation.ENABLED;
        }

    }

    public static void load(Player player) {
        String name = player.getName();
        int id = player.getEntityId();
        YamlConfiguration conf = Configs.load("autosell.yml", Main.getInstance());
        if (conf.contains(String.format("autosell.%s.enabled", name)))
            haveAutoSell.put(id, conf.getBoolean(String.format("autosell.%s.enabled", name)) );
    }

    public static void saveAndUnload(Player player) {
        int id = player.getEntityId();
        String name = player.getName();
        YamlConfiguration conf = Configs.load("autosell.yml", Main.getInstance());

        if (haveAutoSell.containsKey(id) && hasEnabled(player)){
            boolean autosell = haveAutoSell.get(id);

            conf.set(String.format("autosell.%s.enabled", name), autosell);
            Configs.save(conf, "autosell.yml");

            haveAutoSell.remove(Integer.valueOf(id));
        }
        else if (conf.contains(String.format("autosell.%s", name))){
            conf.set(String.format("autosell.%s", name), null);
            Configs.save(conf, "autosell.yml");
        }

    }

}
