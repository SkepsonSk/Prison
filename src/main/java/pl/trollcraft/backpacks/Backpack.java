package pl.trollcraft.backpacks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;

import java.lang.reflect.Array;
import java.util.*;

public class Backpack {

    private static final Random RAND = new Random();

    private static ArrayList<Backpack> backpacks = new ArrayList<>();
    private static Multimap<Integer, Backpack> possessedBackpacks = ArrayListMultimap.create();

    private short id;
    private Inventory inventory;

    // -------- -------- -------- --------

    public Backpack (int size){
        this.id = (short) (RAND.nextInt((Short.MAX_VALUE - Short.MIN_VALUE) + 1) + Short.MIN_VALUE);
        inventory = Bukkit.createInventory(null, size, "Plecak");
        backpacks.add(this);
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
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    // -------- -------- -------- --------

    public static void findBackpacks(Player player) {

        new BukkitRunnable() {

            @Override
            public void run() {
                int playerId = player.getEntityId();
                Inventory inv = player.getInventory();
                possessedBackpacks.get(playerId).clear();
                inv.forEach( i -> {

                    if (i == null || i.getType() != Material.CHEST) return;
                    int id = i.getEnchantmentLevel(Enchantment.DURABILITY);
                    if (id == 0) return;

                    Backpack backpack = get(id);
                    if (backpack == null) return;

                    possessedBackpacks.put(playerId, backpack);
                } );

            }

        }.runTaskAsynchronously(Main.getInstance());

    }

    public static boolean tryAddToBackpacks(Player player, ItemStack itemStack, boolean update) {
        if (update) findBackpacks(player);

        Collection<Backpack> backpacks = possessedBackpacks.get(player.getEntityId());
        for (Backpack backpack : backpacks) {
            if (!backpack.inventory.addItem(itemStack).isEmpty()) continue;
            else return true;
        }


        return false;
    }

    public static ArrayList<ItemStack> getItemsInBackpack(Player player, boolean update) {
        if (update) findBackpacks(player);

        int id = player.getEntityId();
        if (!possessedBackpacks.containsKey(id)) return null;

        Collection<Backpack> pBackpacks = possessedBackpacks.get(id);
        ArrayList<ItemStack> items = new ArrayList<>();

        for (Backpack backpack : pBackpacks)
            items.addAll(Arrays.asList(backpack.inventory.getContents()));

        return items;
    }

    public static void tryRemoveItemsFromBackpacks(Player player, ArrayList<ItemStack> items, boolean update) {
        if (update) findBackpacks(player);

        int id = player.getEntityId();
        if (!possessedBackpacks.containsKey(id)) return;

        Collection<Backpack> pBackpacks = possessedBackpacks.get(id);

        Backpack backpack;
        Iterator<Backpack> pbIt = pBackpacks.iterator();
        while (pbIt.hasNext()){
            backpack = pbIt.next();
            for (ItemStack is : items)
                backpack.inventory.removeItem(is);
        }

    }

    // -------- -------- -------- --------

    public static ArrayList<Backpack> getBackpacks() { return backpacks; }

    // -------- -------- -------- --------

    public void saveSync() {
        YamlConfiguration conf = Configs.load("backpacks.yml", Main.getInstance());
        ItemStack items[] = inventory.getContents(), item;
        conf.set("backpacks." + id + ".size", inventory.getSize());
        for (int i = 0 ; i < items.length ; i++) {

            item = items[i];

            if (item == null || item.getType() == Material.AIR)
                conf.set("backpacks." + id + ".inv." + i + ".exists", false);
            else {
                conf.set("backpacks." + id + ".inv." + i + ".exists", true);
                conf.set("backpacks." + id + ".inv." + i + ".item", item);
            }
        }
        Configs.save(conf, "backpacks.yml");
    }

    public void save() {
        YamlConfiguration conf = Configs.load("backpacks.yml", Main.getInstance());

        new BukkitRunnable() {

            @Override
            public void run() {

                ItemStack items[] = inventory.getContents(), item;
                conf.set("backpacks." + id + ".size", inventory.getSize());
                for (int i = 0 ; i < items.length ; i++) {

                    item = items[i];

                    if (item == null || item.getType() == Material.AIR)
                        conf.set("backpacks." + id + ".inv." + i + ".exists", false);
                    else {
                        conf.set("backpacks." + id + ".inv." + i + ".exists", true);
                        conf.set("backpacks." + id + ".inv." + i + ".item", item);
                    }
                }
                Configs.save(conf, "backpacks.yml");

            }

        }.runTaskAsynchronously(Main.getInstance());

    }

    public static void load(Player player) {
        player.getInventory().forEach( it -> {
            if (it == null || it.getType() != Material.CHEST) return;
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
