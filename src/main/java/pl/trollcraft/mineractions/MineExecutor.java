package pl.trollcraft.mineractions;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.Debug;

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
    private static Queue<Block> blocksCache = new LinkedBlockingQueue<>();

    public static void mine(Player player, ArrayList<Material> materials, ArrayList<Byte> data) {
        mining.add(new Session(player, materials, data));
    }

    public static void addToBlocksCache(Block block) {
        blocksCache.add(block);
    }

    public static void start() {

        new BukkitRunnable() {

            @Override
            public void run() {

                int limit = 50;

                Session s;
                Iterator<Session> it = mining.iterator();

                while (it.hasNext()){

                    if (limit == 0) {
                        Debug.log("Limit reached. Stopping...");
                        break;
                    }
                    limit--;
                    Debug.log(limit);

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
                            action.perform(s.player, s.materials.get(i), s.data.get(i));

                    it.remove();

                }

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 20, 5);

    }

}
