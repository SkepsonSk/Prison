package pl.trollcraft.mineractions.actions;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.mineractions.MineExecutor;
import pl.trollcraft.mineractions.MinerAction;
import pl.trollcraft.obj.booster.Booster;
import pl.trollcraft.selling.SellingUtils;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.DropManager;
import pl.trollcraft.util.Utils;
import tesdev.Money.MoneyAPI;
import tesdev.Money.api.EconomyProfile;

public class AutoSellAction extends MinerAction {

    @Override
    public boolean canBePerformed(Player player) {
        return AutoSell.hasEnabled(player);
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

        if (SellingUtils.hasPrice(itemStack.getType())) {
            double mon = Utils.round(SellingUtils.getPrice(itemStack.getType(), itemStack.getData().getData(), false) * itemStack.getAmount() * SellingUtils.getAutosellFactor() * Booster.getBonus(player), 3);

            EconomyProfile profile = EconomyProfile.get(player);
            profile.addMoney(mon);

        } else player.getInventory().addItem(itemStack);

        return true;
    }

}
