package pl.trollcraft.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBarsPlace (BlockPlaceEvent event) {

        if (event.getBlockPlaced().getType() == Material.IRON_FENCE)
            event.setCancelled(true);

    }

}
