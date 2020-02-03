package pl.trollcraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.trollcraft.util.PrisonBlocksGui;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage();
        if (cmd.equalsIgnoreCase("/warp")) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            PrisonBlocksGui.open(player);
        }
    }

}
