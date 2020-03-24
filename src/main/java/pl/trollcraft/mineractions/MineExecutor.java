package pl.trollcraft.mineractions;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MineExecutor {

    private static class Session {

        private Player player;
        private ArrayList<Material> materials;
        private ArrayList<Byte> data;

        private Session(Player player, ArrayList<Material> materials, ArrayList<Byte> data) {
            this.player = player;
            this.materials = materials;
            this.data = data;
        }

    }

    // --------- -------- -------- --------

    private static Queue<Session> mining = new LinkedBlockingQueue<>();

    public static void mine(Player player, ArrayList<Material> materials, ArrayList<Byte> data) {
        mining.add(new Session(player, materials, data));
    }

    public static void start() {

        new BukkitRunnable() {

            @Override
            public void run() {

                int limit = 250;

                Session s;
                Iterator<Session> it = mining.iterator();

                while (it.hasNext()){

                    if (limit == 0)
                        break;

                    limit--;

                    s = it.next();
                    ArrayList<MinerAction> actions = new ArrayList<>();
                    for (MinerAction action : MinerAction.getActions()) {
                        if (action.canBePerformed(s.player)) {
                            actions.add(action);
                            action.prepare(s.player);
                        }
                    }

                    for (int i = 0 ; i < s.materials.size() ; i++)
                        for (MinerAction action : actions)
                            if (!action.perform(s.player, s.materials.get(i), s.data.get(i))) break;

                    it.remove();

                }

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 20, 5);

    }

}
