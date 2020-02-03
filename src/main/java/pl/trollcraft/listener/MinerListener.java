package pl.trollcraft.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.selling.SellingUtils;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.enchants.BlastEnchantment;
import pl.trollcraft.util.enchants.EnchantRegister;
import tesdev.Money.MoneyAPI;

import java.util.Map;

public class MinerListener implements Listener {

    @EventHandler (priority=EventPriority.LOW)
    public void onMine (BlockBreakEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        Block[] blocks = getBlocksMined(player, event.getBlock());

        if (AutoSell.hasEnabled(player)){
            for (Block b : blocks) {

                if (SellingUtils.hasPrice(b.getType())) {
                    double mon = SellingUtils.getPrice(b.getType());
                    event.getBlock().setType(Material.AIR);
                    MoneyAPI.getInstance().addMoney(event.getPlayer().getName(), mon);
                }
                else player.getInventory().addItem(new ItemStack(b.getType()));

                b.setType(Material.AIR);
            }
        }
        else{
            for (Block b : blocks) {
                player.getInventory().addItem(new ItemStack(b.getType()));
                b.setType(Material.AIR);
            }
        }

    }

    private Block[] getBlocksMined(Player player, Block block) {
        ItemStack itemStack = player.getItemInHand();
        int lvl = hasBlast(itemStack);

        if (lvl != -1)
            return BlastEnchantment.blast(lvl, block);
        return new Block[] {block};
    }

    private int hasBlast(ItemStack i) {
        for (Map.Entry<Enchantment, Integer> en : i.getEnchantments().entrySet()){
            if (en.getKey().getId() == 19060) return en.getValue();
        }
        return -1;
    }

}
