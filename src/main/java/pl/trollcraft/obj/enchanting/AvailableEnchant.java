package pl.trollcraft.obj.enchanting;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.Utils;

import java.util.ArrayList;

public class AvailableEnchant {

    private Enchantment enchantment;
    private String displayName;
    private int price;
    private int level;

    public AvailableEnchant(Enchantment enchantment, String displayName, int price, int level) {
        this.enchantment = enchantment;
        this.displayName = displayName;
        this.price = price;
        this.level = level;
    }

    public AvailableEnchant(Enchantment enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public Enchantment getEnchantment() { return enchantment; }
    public String getDisplayName() { return displayName + " " + Utils.roman(level); }
    public int getPrice() { return price; }

    public void apply(ItemStack itemStack) {
        itemStack.addEnchantment(enchantment, level);
    }

    public static ArrayList<AvailableEnchant> getAvailableEnchants(ItemStack itemStack) {
        ArrayList<AvailableEnchant> availableEnchants = new ArrayList<>();

        Enchantment e;
        int lvl;
        for (EnchantData data : EnchantData.getData()) {
            e = data.getEnchantment();

            if (!e.canEnchantItem(itemStack)) continue;
            lvl = itemStack.getEnchantmentLevel(e);
            Debug.log("" + data.getMaxLevel());
            Debug.log(e.getName() + " " + lvl + " " + data.getMaxLevel());

            if (lvl < data.getMaxLevel()) availableEnchants.add(new AvailableEnchant(e, data.getDisplayName(), data.getLevelPrice() * (lvl + 1), lvl + 1));
        }

        return availableEnchants;
    }

    public static AvailableEnchant get (String itemName) {
        String data[] = itemName.split(" ");
        String name = data[0];
        int lvl = Utils.fromRoman(data[1]);
        for (EnchantData ed : EnchantData.getData()) {
            if (Utils.stringEqual(name, ed.getDisplayName())) {
                return new AvailableEnchant(ed.getEnchantment(), lvl);
            }
        }
        return null;
    }

}
