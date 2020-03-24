package pl.trollcraft.obj.enchanting;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.gui.GUI;
import tesdev.Money.MoneyAPI;
import tesdev.Money.TockensAPI;
import tesdev.Money.api.EconomyProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class EnchantGui {

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

            Consumer<InventoryClickEvent> buy = event -> {
                event.setCancelled(true);

                Player player = (Player) event.getWhoClicked();
                double tokens = EconomyProfile.FastAccess.getTokens(player);

                if (tokens >= ae.getPrice()){
                    ae.apply(player.getItemInHand());
                    reload(player);
                    EconomyProfile.FastAccess.takeTokens(player, ae.getPrice());
                    //TockensAPI.getInstance().removeTockens(player, ae.getPrice());
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Zakupiono enchant!\n&c&l-" + ae.getPrice()));
                }
                else
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&cBrakuje Ci token'ow!"));

            };

            gui.addItem(s, is, buy);
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

        int tokens = EconomyProfile.FastAccess.getTokens(player);
        ItemStack state = new ItemStack(Material.SKULL_ITEM);
        SkullMeta meta = (SkullMeta) state.getItemMeta();
        meta.setDisplayName("§7Token'y: §e" + tokens);
        meta.setOwner(player.getName());
        state.setItemMeta(meta);
        gui.addItem(10, state, event -> event.setCancelled(true));

        gui.open(player);
    }

    public static void reload(Player player) {
        GUI gui = GUI.getOpened(player);
        gui.clear();

        int t = EconomyProfile.FastAccess.getTokens(player);
        ItemStack state = new ItemStack(Material.SKULL_ITEM);
        SkullMeta meta = (SkullMeta) state.getItemMeta();
        meta.setDisplayName("§7Token'y: §e" + t);
        meta.setOwner(player.getName());
        state.setItemMeta(meta);
        gui.addItem(10, state, event -> event.setCancelled(true));

        ArrayList<AvailableEnchant> e = AvailableEnchant.getAvailableEnchants(player.getItemInHand());

        ItemStack is;
        int s = 12;
        for (AvailableEnchant ae : e) {
            is = createItemStack(ae);

            Consumer<InventoryClickEvent> buy = event -> {
                event.setCancelled(true);

                int tokens = EconomyProfile.FastAccess.getTokens(player);

                if (tokens >= ae.getPrice()){
                    ae.apply(player.getItemInHand());
                    EconomyProfile.FastAccess.takeTokens(player, ae.getPrice());
                    reload(player);
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Zakupiono enchant!\n&c&l-" + ae.getPrice()));
                }
                else
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&cBrakuje Ci token'ow!"));

            };

            gui.addItem(s, is, buy);
            s++;
        }

    }

}
