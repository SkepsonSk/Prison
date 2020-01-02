package pl.trollcraft.util.enchants;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

public class EnchantRegister {

    public static final BlastEnchantment BLAST_ENCHANTMENT = new BlastEnchantment();

    public static void register() {

        try {

            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);

            Enchantment.registerEnchantment(BLAST_ENCHANTMENT);
        }
        catch (IllegalAccessException e) { e.printStackTrace(); }
        catch (IllegalStateException e) { e.printStackTrace(); }
        catch (NoSuchFieldException e) { e.printStackTrace(); }

    }

    public static String roman(int a) {
        switch (a){
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            default:
                return "0";
        }
    }

}
