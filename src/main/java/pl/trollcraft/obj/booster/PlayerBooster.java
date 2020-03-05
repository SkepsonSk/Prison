package pl.trollcraft.obj.booster;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;

public class PlayerBooster extends Booster {

    private Player player;

    public PlayerBooster(int seconds, double bonus, Player player) {
        super(seconds, bonus);
        this.player = player;
        Booster.playerBoosters.add(this);
    }

    public Player getPlayer() { return player; }

    public void save() {
        String name = player.getName();
        YamlConfiguration conf = Configs.load("boosters.yml", Main.getInstance());
        conf.set("boosters." + name + ".seconds", seconds);
        conf.set("boosters." + name + ".bonus", bonus);
        Configs.save(conf, "boosters.yml");
    }

    @Override
    public void end() {
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Twoj Booster pieniezny wyczerpal sie..."));
    }

    public static void setOfflineBooster(String name, double bonus, int seconds) {
        YamlConfiguration conf = Configs.load("boosters.yml", Main.getInstance());

        if (conf.contains("boosters." + name)){
            double activeBonus = conf.getDouble("boosters." + name + ".bonus");
            int activeSeconds = conf.getInt("boosters." + name + ".bonus");

            conf.set("boosters." + name + ".bonus", activeBonus + bonus);
            conf.set("boosters." + name + ".seconds", activeSeconds + seconds);
        }
        else {
            conf.set("boosters." + name + ".bonus", bonus);
            conf.set("boosters." + name + ".seconds", seconds);
        }

    }

    public static boolean load (Player player) {

        String name = player.getName();
        YamlConfiguration conf = Configs.load("boosters.yml", Main.getInstance());

        if (conf.contains("boosters." + name)){

            int seconds = conf.getInt("boosters." + name + ".seconds");
            if (seconds <= 0) return false;

            int bonus = conf.getInt("boosters." + name + ".bonus");
            new PlayerBooster(seconds, bonus, player);

            return true;
        }
        return false;
    }

    public static void attemptRemove(Player player) {
        YamlConfiguration conf = Configs.load("boosters.yml", Main.getInstance());
        conf.set("boosters." + player.getName(), null);
        Configs.save(conf, "boosters.yml");
    }

}
