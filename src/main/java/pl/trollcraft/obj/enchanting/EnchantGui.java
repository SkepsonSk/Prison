package pl.trollcraft.obj.enchanting;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.gui.GUI;
import tesdev.Money.MoneyAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class EnchantGui {

    private static Consumer<InventoryClickEvent> buyAction = event -> {
        event.setCancelled(true);
        ItemMeta meta = event.getCurrentItem().getItemMeta();
        AvailableEnchant ae = AvailableEnchant.get(meta.getDisplayName().substring(4));
        Player player = (Player) event.getWhoClicked();
        Debug.log(ae.getEnchantment().getName());
        ae.apply(player.getItemInHand());
        reload(player);
    };

    private static GUI prepare(ItemStack itemStack) {
        ArrayList<AvailableEnchant> e = AvailableEnchant.getAvailableEnchants(itemStack);
        int rows = 3;

        if (e.size() > 5)
            rows += (e.size() / 5) - 1;

        GUI gui = new GUI(rows * 9, "§aEnchantowanie");

        ItemStack is;
        int s = 12;
        for (AvailableEnchant ae : e) {
            is = createItemStack(ae);
            gui.addItem(s, is, buyAction);
            s++;
        }

        return gui;
    }

    private static ItemStack createItemStack (AvailableEnchant enchant) {
        ItemStack item = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§o" + enchant.getDisplayName());
        meta.setLore(Arrays.asList(new String[] {"§7Cena: §e" + enchant.getPrice()}));
        item.setItemMeta(meta);
        return item;
    }

    public static void open(Player player) {
        ItemStack item = player.getItemInHand();
        if (item == null && item.getType() == Material.AIR) return;

        GUI gui = prepare(item);

        double money = MoneyAPI.getInstance().getMoney(player);

        ItemStack state = new ItemStack(Material.SKULL_ITEM);
        SkullMeta meta = (SkullMeta) state.getItemMeta();
        meta.setDisplayName("§7Pieniadze: §e" + money);
        meta.setOwner(player.getName());
        state.setItemMeta(meta);

        gui.addItem(10, state, null);

        gui.open(player);
    }

    public static void reload(Player player) {
        GUI gui = GUI.getOpened(player);
        gui.clear();

        ArrayList<AvailableEnchant> e = AvailableEnchant.getAvailableEnchants(player.getItemInHand());

        ItemStack is;
        int s = 12;
        for (AvailableEnchant ae : e) {
            is = createItemStack(ae);
            gui.addItem(s, is, buyAction);
            s++;
        }

    }

}
