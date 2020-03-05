package pl.trollcraft.selling;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Main;
import pl.trollcraft.obj.ItemPrice;

import java.util.ArrayList;
import java.util.Arrays;

public class SellingUtils {

    private static final String SELLS_PATH = "selling";

    //private static HashMap<Integer, Double> prices = new HashMap<>();
    private static ArrayList<ItemPrice> prices = new ArrayList<>();
    private static int[] materials;

    private static double autosellFactor = 1;

    public static void calcPrices() {
        prices.clear();
        YamlConfiguration config = Main.getSells();
        int len = config.getStringList(SELLS_PATH).size(), i = 0;
        materials = new int[len];
        autosellFactor = config.getDouble("autosellFactor");

        for (String s : config.getStringList(SELLS_PATH)){
            String[] data = s.split(":");
            Material m = Material.getMaterial(data[0].toUpperCase());
            byte d = Byte.parseByte(data[1]);
            double p = Double.parseDouble(data[2]);

            prices.add(new ItemPrice(m, d, p));

            //prices.put(Utils.encodeMaterial(m, d), Double.parseDouble(data[2]));
            materials[i] = m.getId();
            i++;
        }
        Arrays.sort(materials);
    }

    public static double getAutosellFactor() { return autosellFactor; }

    public static double getPrice(Material material, byte data, boolean safe) {
        if (material == null) return 0d;

        for (ItemPrice ip : prices)
            if (ip.getMaterial() == material && ip.getData() == data) return ip.getPrice();
        return 0;
    }

    public static boolean hasPrice(Material material){
        int i = Arrays.binarySearch(materials, material.getId());
        return i >= 0;
    }

}
