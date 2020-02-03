package pl.trollcraft.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

public class GUI {

    private static HashMap<Integer, GUI> opened = new HashMap<>();

    private Inventory inventory;
    private HashMap<Integer, Consumer<InventoryClickEvent>> listener;

    public GUI (int slots, String title) {
        inventory = Bukkit.createInventory(null, slots, title);
        listener = new HashMap<>();
    }

    public void addItem(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> click){
        listener.put(slot, click);
        inventory.setItem(slot, itemStack);
    }

    public void clear() {
        listener.clear();
        inventory.clear();
    }

    public Consumer<InventoryClickEvent> getClick(int slot){
        if (listener.containsKey(slot))
            return listener.get(slot);
        return null;
    }

    public void open(Player player) {
        int id = player.getEntityId();
        if (opened.containsKey(id)) return;
        opened.put(id, this);
        player.openInventory(inventory);
    }

    public void close(Player player) {
        int id = player.getEntityId();
        if (opened.containsKey(id)) opened.remove(id);
    }

    public static GUI getOpened(Player player){
        int id = player.getEntityId();
        if (opened.containsKey(id)) return opened.get(id);
        return null;
    }

}
