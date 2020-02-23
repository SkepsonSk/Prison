package pl.trollcraft.obj.enchanting;

import org.bukkit.enchantments.Enchantment;
import pl.trollcraft.util.enchants.EnchantRegister;

public enum Enchant {

    EFFICIENCY(Enchantment.DIG_SPEED),
    UNBREAKING(Enchantment.DURABILITY),
    FORTUNE(Enchantment.LOOT_BONUS_BLOCKS),
    BLAST(EnchantRegister.BLAST_ENCHANTMENT);

    private Enchantment enchantment;

    Enchant(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public Enchantment getEnchantment() { return enchantment; }

}
