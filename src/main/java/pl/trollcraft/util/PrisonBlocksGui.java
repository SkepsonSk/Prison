package pl.trollcraft.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.obj.PrisonBlock;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.util.gui.GUI;

import java.util.Arrays;
import java.util.function.Consumer;

public class PrisonBlocksGui {

    private static final Material UNLOCKED = Material.EMERALD_BLOCK;
    private static final Material LOCKED = Material.REDSTONE_BLOCK;

    // -------- -------- -------- -------- --------

    private static final Consumer<InventoryClickEvent> blockPick = event -> {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        ItemMeta itemMeta = clicked.getItemMeta();
        String warpName = itemMeta.getDisplayName().substring(13);
        Warp warp = Warp.get(warpName);
        warp.teleport(player);
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Przenoszenie na blok &e" + warpName + "."));
    };

    private static final Consumer<InventoryClickEvent> lockedPick = event -> event.setCancelled(true);

    // -------- -------- -------- -------- --------

    private static GUI prepare(Player player) {
        GUI gui = new GUI(54, "§7§lBloki");
        int locked = PrisonBlock.getBlocks().size(), index = 0;
        for (PrisonBlock pb : PrisonBlock.getBlocks()) {

            if (pb.hasUnlocked(player)) {
                gui.addItem(index, createUnlockedItem(pb), blockPick);
                index++;
                locked--;
            }
            else break;

        }

        if (locked > 0) {

            for (int i = 0 ; i < locked ; i++) {
                gui.addItem(index, createLockedItem(), lockedPick);
                index++;
            }

        }

        return gui;
    }

    private static ItemStack createUnlockedItem(PrisonBlock pb) {
        ItemStack itemStack = new ItemStack(UNLOCKED);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§a§lBlok §e§l" + pb.getName());
        itemMeta.setLore(Arrays.asList( new String[] {"", "§aBLOK ODBLOKOWANY!", ""} ) );
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static ItemStack createLockedItem() {
        ItemStack itemStack = new ItemStack(LOCKED);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§c§lBlok NIEDOSTEPNY");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    // -------- -------- -------- -------- --------

    public static void open(Player player) {
        prepare(player).open(player);
    }

}
