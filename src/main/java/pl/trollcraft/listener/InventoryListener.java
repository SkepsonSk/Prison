package pl.trollcraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import pl.trollcraft.util.gui.GUI;
import pl.trollcraft.util.gui.Item;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick (InventoryClickEvent event) {

        if (event.getClickedInventory() == null) return;
        if (!event.getClickedInventory().getName().contains("§"))
            return;

        Player player = (Player) event.getWhoClicked();
        GUI gui = GUI.getOpened(player);
        Item item = gui.getItem(event.getSlot());

        Bukkit.getConsoleSender().sendMessage("[DEBUG] Clicked");

        if (item != null)
            item.getOnClick().accept(event);

    }

    @EventHandler
    public void onClose (InventoryCloseEvent event) {
        if (!event.getInventory().getName().contains("§"))
            return;

        Player player = (Player) event.getPlayer();
        GUI gui = GUI.getOpened(player);

        if (gui != null) gui.close(player);
    }

}
