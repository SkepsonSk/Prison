package pl.trollcraft.obj.cells;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;

import java.util.ArrayList;
import java.util.Iterator;

public class PendingVisit {

    private static ArrayList<PendingVisit> pendingVisits = new ArrayList<>();

    private Player visitor;
    private Cell cell;
    private long time;
    private boolean accepted;

    public PendingVisit(Player visitor, Cell cell) {
        this.visitor = visitor;
        this.cell = cell;
        time = System.currentTimeMillis() + 1000L * 60L;
        accepted = false;
        pendingVisits.add(this);
    }

    public String getVisitorName() { return visitor.getName(); }
    public void teleport() {
        visitor.teleport(cell.getLocation());
    }

    public static void startTimer() {

        new BukkitRunnable() {

            @Override
            public void run() {

                PendingVisit pv;
                long now = System.currentTimeMillis();
                Iterator<PendingVisit> it = pendingVisits.iterator();
                while (it.hasNext()){

                    pv = it.next();
                    if (now >= pv.time){

                        if (pv.cell == null || !pv.cell.getPlayer().isOnline()){
                            ChatUtil.sendMessage(pv.visitor, ChatUtil.fixColor("&cTwoja prosba odwiedzin wieznia &e"
                                    + pv.cell.getPlayer().getName() + " &7zostala uniewazniona (gracz opuscil serwer)."));
                            continue;
                        }

                        ChatUtil.sendMessage(pv.visitor, ChatUtil.fixColor("&cTwoja prosba odwiedzin wieznia &e"
                                + pv.cell.getPlayer().getName() + " &7przedawnila sie."));
                        it.remove();
                        continue;
                    }
                    else if (pv.accepted){
                        ChatUtil.sendMessage(pv.visitor, ChatUtil.fixColor("&7Wiezien &e" + pv.cell.getPlayer() + " &7zezwala na Twoje odwiedziny."));
                        pv.teleport();
                        it.remove();
                    }

                }

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 20, 20);

    }

    public static boolean isAlreadyVisiting(Player player, Cell cell){
        for (PendingVisit pv : pendingVisits) {
            if (pv.visitor.getEntityId() == player.getEntityId() &&
                    pv.cell.getPlayer().getEntityId() == cell.getPlayer().getEntityId()) return true;
        }
        return false;
    }

    public static boolean accept(Player player, Player visitor) {

        for (PendingVisit pv : pendingVisits) {
            if (pv.visitor.getEntityId() == visitor.getEntityId() &&
                    pv.cell.getPlayer().getEntityId() == player.getEntityId()){
                pv.accepted = true;
                return true;
            }
        }
        return false;
    }

    public static ArrayList<PendingVisit> getPendingVisits(Player player) {
        ArrayList<PendingVisit> visits = new ArrayList<>();
        for (PendingVisit pv : pendingVisits) {
            if (pv.cell.getPlayer().getEntityId() == player.getEntityId()) visits.add(pv);
        }
        return visits;
    }

}
