package pl.trollcraft.backpacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.Debug;

import java.util.ArrayList;
import java.util.Random;

public class Backpack {

    private static final Random RAND = new Random();

    private static ArrayList<Backpack> backpacks = new ArrayList<>();

    private short id;
    private Inventory inventory;

    // -------- -------- -------- --------

    public Backpack (int size){
        this.id = (short) (RAND.nextInt((Short.MAX_VALUE - Short.MIN_VALUE) + 1) + Short.MIN_VALUE);
        inventory = Bukkit.createInventory(null, size, "Plecak");
        backpacks.add(this);
        Debug.log(id);
    }

    public Backpack(short id, Inventory inventory) {
        this.id = id;
        this.inventory = inventory;
        backpacks.add(this);
    }

    // -------- -------- -------- --------

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(Material.CHEST);
        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, id);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§fPlecak");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§fSlotow: §e" + inventory.getSize());
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    // -------- -------- -------- --------

    public void save() {
        YamlConfiguration conf = Configs.load("backpacks.yml", Main.getInstance());
        ItemStack items[] = inventory.getContents(), item;
        conf.set("backpacks." + id + ".size", inventory.getSize());
        for (int i = 0 ; i < items.length ; i++) {

            item = items[i];

            if (item == null || item.getType() == Material.AIR)
                conf.set("backpacks." + id + ".inv." + i + "exists", false);
            else {
                conf.set("backpacks." + id + ".inv." + i + "exists", true);
                conf.set("backpacks." + id + ".inv." + i + "item", item);
            }
        }
        Configs.save(conf, "backpacks.yml");
    }

    public static void load(Player player) {
        player.getInventory().forEach( it -> {
            if (it.getType() == null || it.getType() != Material.CHEST) return;
            short id = (short) it.getEnchantmentLevel(Enchantment.DURABILITY);
            load(id);
        } );
    }

    public static void load(short id) {
        YamlConfiguration conf = Configs.load("backpacks.yml", Main.getInstance());
        if (!conf.contains("backpacks." + id)) return;

        int size = conf.getInt("backpacks." + id + ".size");
        Inventory inventory = Bukkit.createInventory(null, size, "Plecak");
        ItemStack item;

        for (int i = 0 ; i < size ; i++) {
            if (conf.getBoolean("backpacks." + id + ".inv." + i + ".exists")){
                item = conf.getItemStack("backpacks." + id + ".inv." + i + ".item");
                inventory.setItem(i, item);
            }
        }

        new Backpack(id, inventory);
    }

    public static Backpack get(int id) {
        for (Backpack bp : backpacks)
            if (bp.id == id) return bp;
        return null;
    }

}
