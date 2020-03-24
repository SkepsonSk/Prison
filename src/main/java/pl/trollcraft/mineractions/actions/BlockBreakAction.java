package pl.trollcraft.mineractions.actions;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.backpacks.Backpack;
import pl.trollcraft.mineractions.MinerAction;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.DropManager;
import pl.trollcraft.util.Utils;

public class BlockBreakAction extends MinerAction {

    @Override
    public boolean canBePerformed(Player player) {
        return !AutoSell.hasEnabled(player);
    }

    @Override
    public void prepare(Player player) {
        int fortuneLvl = player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
        registerData("fortuneLvl", fortuneLvl);
    }

    @Override
    public boolean perform(Player player, Material material, byte data) {

        int fortuneLvl = (int) getData("fortuneLvl");

        ItemStack itemStack = DropManager.getItem(material.getId(), data, fortuneLvl);
        if (itemStack == null) itemStack = new ItemStack(material, 1, data);

        if (!player.getInventory().addItem(itemStack).isEmpty()) {

            if (!Backpack.tryAddToBackpacks(player, itemStack, false)){
                Utils.send("§cTwoj ekwipunek", EnumWrappers.TitleAction.TITLE, 5, 20, 5, player);
                Utils.send("§cjest §ePELNY!", EnumWrappers.TitleAction.SUBTITLE, 5, 20, 5, player);
                return false;
            }

            return true;

        }

        return true;

    }

}
