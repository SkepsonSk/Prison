package pl.trollcraft.obj;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;

import java.util.ArrayList;
import java.util.Iterator;

public class Booster {

    private static ArrayList<Booster> boosters = new ArrayList<>();

    private Player player;
    private long until;
    private double bonus;

    public Booster(Player player, long until, double bonus) {
        this.player = player;
        this.until = until;
        this.bonus = bonus;
        boosters.add(this);
    }

    public static void listen() {

        new BukkitRunnable() {

            @Override
            public void run() {

                
                Iterator<Booster> it = boosters.iterator();
                while (it.hasNext()){



                }

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 20 * 5, 20 * 5);

    }

}
