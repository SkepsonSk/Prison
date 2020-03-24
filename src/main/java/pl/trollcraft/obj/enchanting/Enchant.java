package pl.trollcraft.obj.enchanting;

import org.bukkit.enchantments.Enchantment;
import pl.trollcraft.util.enchants.EnchantRegister;

public enum Enchant {

    EFFICIENCY(Enchantment.DIG_SPEED),
    UNBREAKING(Enchantment.DURABILITY),
    FORTUNE(Enchantment.LOOT_BONUS_BLOCKS),
    BLAST(EnchantRegister.BLAST_ENCHANTMENT),

    PROTECTION(Enchantment.PROTECTION_ENVIRONMENTAL),
    FIRE_PROTECTION(Enchantment.PROTECTION_FIRE),
    PROTECTION_EXPLOSIONS(Enchantment.PROTECTION_EXPLOSIONS),
    PROTECTION_PROJECTILE(Enchantment.PROTECTION_PROJECTILE),
    THORNS(Enchantment.THORNS),

    FLY(EnchantRegister.FLY_ENCHANTMENT);

    private Enchantment enchantment;

    Enchant(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public Enchantment getEnchantment() { return enchantment; }

}
