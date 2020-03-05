package pl.trollcraft.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;

public class BackpackListener implements Listener {

    @EventHandler
    public void onInteract (PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.CHEST) return;

        int id = item.getEnchantmentLevel(Enchantment.DURABILITY);
        Backpack backpack = Backpack.get(id);

        Bukkit.getConsoleSender().sendMessage("" + id);

        if (backpack == null) return;

        backpack.open(event.getPlayer());
    }

    @EventHandler
    public void onClose (InventoryCloseEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (item == null || item.getType() != Material.CHEST) return;

        int id = item.getEnchantmentLevel(Enchantment.DURABILITY);
        Backpack backpack = Backpack.get(id);

        if (backpack == null) return;

        new BukkitRunnable() {

            @Override
            public void run() {
                backpack.save();
            }

        }.runTaskAsynchronously(Main.getInstance());
    }

}
