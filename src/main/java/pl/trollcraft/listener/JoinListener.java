package pl.trollcraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.trollcraft.obj.PrisonBlock;
import pl.trollcraft.obj.cells.Cell;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.MinersManager;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AutoSell.load(player);
        Cell.load(player);
        PrisonBlock.assignToPlayer(player);
        MinersManager.load(player);

        Warp.get("spawn").teleport(player);
    }

}
