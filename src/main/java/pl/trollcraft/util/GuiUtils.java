package pl.trollcraft.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.util.gui.GUI;
import pl.trollcraft.util.gui.Item;

public class GuiUtils {

    public static GUI gui;

    public static void createInventory() {

        ItemStack grass = new ItemStack(Material.GRASS);
        ItemStack stone = new ItemStack(Material.STONE);
        ItemStack log = new ItemStack(Material.LOG);

        Item grassItem = new Item(0, grass, e -> {
            e.getWhoClicked().sendMessage("Hej, hej, hej");
        });

        Item stoneItem = new Item(2, stone, e -> {
            e.getWhoClicked().sendMessage("STONKS");
            e.setCancelled(true);
        });

        Item logItem = new Item(5, log, e -> {
            e.getWhoClicked().sendMessage("POGERS");
        });

        gui = new GUI(9*3, "§0§lKOCHAM LASAGNE");
        gui.addItem(grassItem);
        gui.addItem(stoneItem);
        gui.addItem(logItem);

    }

}
