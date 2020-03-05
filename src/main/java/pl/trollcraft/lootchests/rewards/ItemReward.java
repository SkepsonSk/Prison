package pl.trollcraft.lootchests.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemReward implements Reward {

    public ItemStack itemStack;

    public ItemReward (ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(itemStack);
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
    }
}
