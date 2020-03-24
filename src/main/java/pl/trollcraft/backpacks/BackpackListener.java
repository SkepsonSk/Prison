package pl.trollcraft.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.obj.warps.PlayerWarpEvent;
import pl.trollcraft.util.Debug;

public class BackpackListener implements Listener {

    @EventHandler
    public void onInteract (PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.CHEST) return;

        int id = item.getEnchantmentLevel(Enchantment.DURABILITY);
        Backpack backpack = Backpack.get(id);

        if (backpack == null) return;

        backpack.open(event.getPlayer());
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event) {
        if (!event.getInventory().getName().contains("Plecak")) return;

        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() != Material.CHEST) return;
        if (item.getEnchantmentLevel(Enchantment.DURABILITY) == 0) return;

        event.setCancelled(true);
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

    @EventHandler
    public void onWarp (PlayerWarpEvent event) {
        Player player = event.getPlayer();
        Backpack.findBackpacks(player);
    }

}
