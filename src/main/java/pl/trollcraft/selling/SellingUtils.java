package pl.trollcraft.selling;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;

import java.util.Arrays;
import java.util.HashMap;

public class SellingUtils {

    private static final String SELLS_PATH = "selling";

    private static HashMap<Material, Double> prices = new HashMap<>();
    private static int[] materials;

    public static void calcPrices() {
        prices.clear();
        YamlConfiguration config = Main.getSells();
        int len = config.getStringList(SELLS_PATH).size(), i = 0;
        materials = new int[len];
        for (String s : config.getStringList(SELLS_PATH)){
            Bukkit.getConsoleSender().sendMessage(s);
            String[] data = s.split("=");
            Material m = Material.valueOf(data[0].toUpperCase());
            prices.put(m, Double.parseDouble(data[1]));
            materials[i] = m.ordinal();
            i++;
        }
        Arrays.sort(materials);
    }

    public static double getPrice(Material material) {
        return prices.get(material);
    }

    public static boolean hasPrice(Material material){
        int i = Arrays.binarySearch(materials, material.ordinal());
        return i >= 0;
    }

}
