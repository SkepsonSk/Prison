package pl.trollcraft.util.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class FortuneEnchant extends Enchantment {

    public FortuneEnchant() {
        super(19061);
    }

    @Override
    public String getName() {
        return "Fortuna";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }

}
