package pl.trollcraft.obj.enchanting;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;

import java.util.ArrayList;

public class EnchantData {

    private static ArrayList<EnchantData> data = new ArrayList<>();

    private Enchantment enchantment;
    private String displayName;
    private int maxLevel;
    private int levelPrice;

    public EnchantData(Enchantment enchantment, String displayName, int maxLevel, int levelPrice) {
        this.enchantment = enchantment;
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.levelPrice = levelPrice;
        data.add(this);
    }

    public static ArrayList<EnchantData> getData() { return data; }

    public String getDisplayName() { return displayName; }
    public Enchantment getEnchantment() { return enchantment; }
    public int getMaxLevel() { return maxLevel; }
    public int getLevelPrice() { return levelPrice; }

    public static void load() {
        YamlConfiguration conf = Configs.load("enchantdata.yml", Main.getInstance());
        conf.getConfigurationSection("enchantdata").getKeys(false).forEach( s -> {
            Enchantment enchantment = Enchant.valueOf(s.toUpperCase()).getEnchantment();
            String displayName = conf.getString(String.format("enchantdata.%s.displayName", s));
            int maxLevel  = conf.getInt(String.format("enchantdata.%s.maxLevel", s));
            int levelPrice  = conf.getInt(String.format("enchantdata.%s.levelPrice", s));
            new EnchantData(enchantment, displayName, maxLevel, levelPrice);
        } );

    }

}
