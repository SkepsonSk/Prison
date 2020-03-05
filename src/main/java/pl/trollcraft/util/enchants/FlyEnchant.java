package pl.trollcraft.util.enchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class FlyEnchant extends Enchantment {

    public FlyEnchant() {
        super(19062);
    }

    @Override
    public String getName() {
        return "Lot";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        Material m = itemStack.getType();
        return (m == Material.DIAMOND_BOOTS || m == Material.LEATHER_BOOTS ||
                m == Material.IRON_BOOTS || m == Material.CHAINMAIL_BOOTS ||
                m == Material.GOLD_BOOTS);
    }

}
