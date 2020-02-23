package pl.trollcraft.selling;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Main;

import java.util.Arrays;
import java.util.HashMap;

public class SellingUtils {

    private static final String SELLS_PATH = "selling";

    private static HashMap<Integer, Double> prices = new HashMap<>();
    private static int[] materials;

    public static void calcPrices() {
        prices.clear();
        YamlConfiguration config = Main.getSells();
        int len = config.getStringList(SELLS_PATH).size(), i = 0;
        materials = new int[len];
        for (String s : config.getStringList(SELLS_PATH)){
            String[] data = s.split(":");
            Material m = Material.getMaterial(data[0].toUpperCase());
            byte d = Byte.parseByte(data[1]);
            prices.put(m.ordinal() + d, Double.parseDouble(data[2]));
            materials[i] = m.ordinal();
            i++;
        }
        Arrays.sort(materials);
    }

    public static double getPrice(Material material, byte data) {
        return prices.get(material.ordinal() + data);
    }

    public static boolean hasPrice(Material material){
        int i = Arrays.binarySearch(materials, material.ordinal());
        return i >= 0;
    }

}
