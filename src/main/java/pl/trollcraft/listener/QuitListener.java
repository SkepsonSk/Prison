package pl.trollcraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.trollcraft.obj.cells.Cell;
import pl.trollcraft.util.AutoSell;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit (PlayerQuitEvent event) {
        Player player = event.getPlayer();
        AutoSell.saveAndUnload(player);

        Cell cell = Cell.get(player);
        cell.save();
        cell.unload();
    }

}
