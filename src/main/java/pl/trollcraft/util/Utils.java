package pl.trollcraft.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Utils {

    public static String roman(int n) {
        switch (n) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";

            default:
                return "";
        }
    }

    public static int fromRoman(String r) {
        if (r.equals("I")) return 1;
        else if (r.equals("II")) return 2;
        else if (r.equals("III")) return 3;
        else if (r.equals("IV")) return 4;
        else if (r.equals("V")) return 5;
        else if (r.equals("VI")) return 6;

        return 0;
    }

    public static boolean stringEqual(String a, String b) {
        if (a.length() != b.length()) return false;
        for (int i = 0 ; i < a.length() ; i++) {
            if (a.charAt(i) != b.charAt(i)) return false;
        }
        return true;
    }

    public static ItemStack deserialize(YamlConfiguration conf, String path) {

        Material type = Material.getMaterial(conf.getString(path + ".type"));
        byte data = 0;

        if (conf.contains(path + ".data"))
            data = Byte.parseByte(conf.getString(path + ".data"));

        int amount = 1;
        if (conf.contains(path + ".amount"))
            amount = conf.getInt(path + ".amount");

        String name = null;
        List<String> lore = null;
        if (conf.contains(path + ".name"))
            name = conf.getString(path + ".name");
        if (conf.contains(path + ".lore"))
            lore = conf.getStringList(path + ".lore");

        ItemStack itemStack = new ItemStack(type, amount, data);

        if (conf.contains(path + ".enchantments")){
            conf.getStringList(path + ".enchantments").forEach( e -> {
                String[] enchData = e.split(":");
                Enchantment enchantment = Enchantment.getByName(enchData[0]);
                int level = Integer.parseInt(enchData[1]);
                itemStack.addEnchantment(enchantment, level);
            } );
        }

        if (name != null || lore != null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (name != null)
                itemMeta.setDisplayName(name);
            if (lore != null)
                itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

}
