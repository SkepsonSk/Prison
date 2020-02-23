package pl.trollcraft.mineractions.actions;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;
import pl.trollcraft.mineractions.MinerAction;
import pl.trollcraft.selling.SellingUtils;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.DropManager;
import tesdev.Money.MoneyAPI;

public class AutoSellAction extends MinerAction {

    private static WorldGuardPlugin worldGuardPlugin = Main.getWorldGuardPlugin();

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
    public void perform(Player player, Block block) {
        int fortuneLvl = (int) getData("fortuneLvl");

        byte data = block.getData();

        ItemStack itemStack = DropManager.getItem(block.getTypeId(), data, fortuneLvl);
        if (itemStack == null) itemStack = new ItemStack(block.getType(), 1, data);

        if (SellingUtils.hasPrice(itemStack.getType())) {
            double mon = SellingUtils.getPrice(itemStack.getType(), itemStack.getData().getData());
            block.setType(Material.AIR);
            MoneyAPI.getInstance().addMoney(player, mon);
        } else player.getInventory().addItem(itemStack);

        block.setType(Material.AIR);
    }

}
