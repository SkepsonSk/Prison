package pl.trollcraft.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onEndPortalBreak (BlockBreakEvent event) {
        Material material = event.getBlock().getType();
        if (material == Material.ENDER_PORTAL) event.setCancelled(true);
    }

}
