package pl.trollcraft.util.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class Item {

    private int slot;
    private ItemStack itemStack;
    private Consumer<InventoryClickEvent> onClick;

    public Item(int slot, ItemStack itemStack, Consumer<InventoryClickEvent> onClick) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.onClick = onClick;
    }

    public int getSlot() { return slot; }
    public ItemStack getItemStack() { return itemStack; }
    public Consumer<InventoryClickEvent> getOnClick() { return onClick; }
}
