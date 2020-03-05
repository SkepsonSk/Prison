package pl.trollcraft.envoy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;

import java.util.ArrayList;
import java.util.Collections;

public class EnvoyItem implements Comparable<EnvoyItem>{

    private static ArrayList<EnvoyItem> envoyItems = new ArrayList<>();

    private ItemStack itemStack;
    private double chance;

    public EnvoyItem (ItemStack itemStack, double chance) {
        this.itemStack = itemStack;
        this.chance = chance;
        envoyItems.add(this);
    }

    public ItemStack getItemStack() { return itemStack; }
    public double getChance() { return chance; }

    @Override
    public int compareTo(EnvoyItem o) {
        if (chance > o.chance) return 1;
        else if (chance < o.chance) return -1;
        return 0;
    }

    // -------- -------- -------- -------- -------- --------

    public static ArrayList<EnvoyItem> getEnvoyItems() { return envoyItems; }

    public static void registerItemStack(ItemStack itemStack, double chance) {
        new EnvoyItem(itemStack, chance);
        YamlConfiguration conf = Configs.load("envoyitems.yml", Main.getInstance());
        int id = conf.getInt("nextId");
        conf.set("envoyitems." + id + ".item", itemStack);
        conf.set("envoyitems." + id + ".chance", chance);
        conf.set("nextId", id + 1);
        Configs.save(conf, "envoyitems.yml");
    }

    public static void loadEnvoyItems() {
        YamlConfiguration conf = Configs.load("envoyitems.yml", Main.getInstance());

        conf.getConfigurationSection("envoyitems").getKeys(false).forEach( s -> {
            ItemStack itemStack = conf.getItemStack("envoyitems." + s + ".item");
            double chance = conf.getDouble("envoyitems." + s + ".chance");
            new EnvoyItem(itemStack, chance);
        } );

        Collections.sort(envoyItems);

        for (EnvoyItem envoyItem : envoyItems) {

            Bukkit.getConsoleSender().sendMessage(envoyItem.getChance() + "");

        }
    }

}
