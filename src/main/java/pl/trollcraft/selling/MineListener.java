package pl.trollcraft.selling;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import tesdev.Money.MoneyAPI;

public class MineListener implements Listener {

    @EventHandler
    public void onMine (BlockBreakEvent event) {
        Material m = event.getBlock().getType();
        if (SellingUtils.hasPrice(m)){
            event.setCancelled(true);
            double mon = SellingUtils.getPrice(m);
            event.getBlock().setType(Material.AIR);
            MoneyAPI.getInstance().addMoney(event.getPlayer().getName(), mon);
        }
    }

}
