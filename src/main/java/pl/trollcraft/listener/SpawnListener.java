package pl.trollcraft.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class SpawnListener implements Listener {

    @EventHandler
    public void onSpawn (EntitySpawnEvent event) {
        String world = event.getLocation().getWorld().getName();
        if (world.equalsIgnoreCase("envoy"))
            event.setCancelled(true);
    }

}
