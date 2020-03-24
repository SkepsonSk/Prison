package pl.trollcraft.obj.booster;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Booster {

    private static ArrayList<Booster> boosters = new ArrayList<>();

    protected static ArrayList<GlobalBooster> globalBoosters = new ArrayList<>();
    protected static ArrayList<PlayerBooster> playerBoosters = new ArrayList<>();

    protected int seconds;
    protected double bonus;

    public Booster(int seconds, double bonus) {
        this.seconds = seconds;
        this.bonus = bonus;
        boosters.add(this);
    }

    public boolean hasExpired() { return seconds <= 0; }
    public int getSeconds() { return seconds; }
    public double getBonus() { return bonus; }

    public void setSeconds(int seconds) { this.seconds = seconds; }
    public void setBonus(double bonus) { this.bonus = bonus; }

    public abstract void end();

    // -------- -------- --------- --------- ---------

    public static void listen() {

        new BukkitRunnable() {

            @Override
            public void run() {

                Iterator<Booster> it = boosters.iterator();
                Booster b;
                while (it.hasNext()) {

                    b = it.next();
                    b.seconds--;

                    if (b.seconds <= 0){
                        b.end();
                        it.remove();
                    }

                }

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 20, 20);

    }

    public static PlayerBooster getBooster (Player player) {
        int id = player.getEntityId();
        for (PlayerBooster playerBooster : playerBoosters){
            if (playerBooster.getPlayer().getEntityId() == id) return playerBooster;
        }
        return null;
    }

    public static ArrayList<GlobalBooster> getGlobalBoosters() { return globalBoosters; }

    public static void sendGlobalBoosterInfo(Player player) {
        if (globalBoosters.isEmpty()) return;

        ChatUtil.sendMessage(player, ChatUtil.fixColor("&eAktywne Booster'y:"));
        for (GlobalBooster globalBooster : globalBoosters)
            ChatUtil.sendMessage(player, ChatUtil.fixColor("  &e&l+ &7Bonus: x" + globalBooster.bonus + " &7przez &e" + globalBooster.getSeconds() + " sekund"));
    }

    public static double getBonus(Player player) {
        double bonus = 0;
        for (GlobalBooster booster : globalBoosters)
            bonus += booster.bonus;

        PlayerBooster playerBooster = PlayerBooster.getBooster(player);
        if (playerBooster != null)
            bonus += playerBooster.bonus;

        return bonus > 0 ? bonus : 1;
    }

}
