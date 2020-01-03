package pl.trollcraft.util.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class GUI {

    private static HashMap<Integer, GUI> opened = new HashMap<>();

    private int slots;
    private String title;
    private ArrayList<Item> items;
    private HashMap<Integer, Inventory> inventories;

    public GUI (int slots, String title) {
        this.slots = slots;
        this.title = title;
        items = new ArrayList<>();
        inventories = new HashMap<>();
        if (title.contains("§"))
            throw new RuntimeException("Missin PARAGRAPH in inv title.");
    }

    public void addItem(Item item) { items.add(item); }

    public Item getItem(int slot){
        for (Item i : items){
            if (i.getSlot() == slot) return i;
        }
        return null;
    }

    public Inventory create(Player player) {
        Inventory inv = getInventory(player);
        if (inv != null) return inv;

        inv = Bukkit.createInventory(null, slots, title);
        for (Item i : items)
            inv.setItem(i.getSlot(), i.getItemStack());

        int id = player.getEntityId();

        inventories.put(id, inv);
        if (opened.containsKey(id)) opened.replace(id, this);
        else opened.put(id, this);

        return inv;
    }

    public void close(Player player) {
        int id = player.getEntityId();
        if (opened.containsKey(id)) opened.remove(id);
        if (inventories.containsKey(id)) inventories.remove(id);
    }

    private Inventory getInventory(Player player) {
        int id = player.getEntityId();
        if (inventories.containsKey(id))
            return inventories.get(id);
        return null;
    }

    public static GUI getOpened(Player player){
        int id = player.getEntityId();
        if (opened.containsKey(id)) return opened.get(id);
        return null;
    }

}
