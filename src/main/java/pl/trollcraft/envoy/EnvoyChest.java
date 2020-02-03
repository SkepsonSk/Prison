package pl.trollcraft.envoy;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;

import java.util.*;

public class EnvoyChest {

    private static long next;

    public static World ENVOY_WORLD;
    public static final Random RANDOM = new Random();

    private static ArrayList<EnvoyChest> chests = new ArrayList<>();
    private static HashMap<ItemStack, Double> envoyItems = new HashMap<>();

    private int x, y, z;
    private boolean opened;

    // -------- -------- -------- -------- -------- --------

    public EnvoyChest(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        opened = false;
        chests.add(this);
    }

    public boolean hasBeenOpened() { return opened; }
    public void setHasBeenOpened(boolean opened) { this.opened = opened; }

    public void spawnItems(Chest chest) {
        Inventory inv = chest.getBlockInventory();
        inv.clear();
        int slot = RANDOM.nextInt(24);
        inv.setItem(slot, getRandomItemStack());
    }

    // -------- -------- -------- -------- -------- --------

    private static boolean envoy = false;
    public static boolean envoyOn () { return envoy; }

    public static long whenNext() { return next; }

    public static void startEnvoy() {
        if (envoy) return;
        for (EnvoyChest e : chests)
            e.opened = false;
        String message = ChatUtil.fixColor("&7Rozpoczyna sie zrzut! Chcesz zawalczyc o cenne lupy? Wpisz komende /przemyt.");
        envoy = true;
        for (Player p : Bukkit.getOnlinePlayers())
            ChatUtil.sendMessage(p, message);
    }

    public static void stopEnvoy() {
        if (!envoy) return;
        String message = ChatUtil.fixColor("&7Zrzut zakonczyl sie! Kolejny za &e8 minut! Aby dowiedziec sie ile czasu pozostalo, uzyj /zrzut");
        envoy = false;
        for (Player p : Bukkit.getOnlinePlayers())
            ChatUtil.sendMessage(p, message);
        next = System.currentTimeMillis() + 10L * 60L * 1000L;
    }

    // -------- -------- -------- -------- -------- --------

    public static void init() {
        next = System.currentTimeMillis() + 10L * 60L * 1000L;
        new BukkitRunnable() {

            @Override
            public void run() {
                if (envoy) return;
                if (System.currentTimeMillis() >= next) startEnvoy();
            }

        }.runTaskTimer(Main.getInstance(), 1000L * 2L, 5L * 1000L);
    }

    private static ItemStack getRandomItemStack() {
        double c = 0 + (100 - 0) * RANDOM.nextDouble();
        Set<Map.Entry<ItemStack, Double>> chances = envoyItems.entrySet();
        for (Map.Entry<ItemStack, Double> i : chances) {
            if (c <= i.getValue()) return i.getKey();
        }
        return ((Map.Entry<ItemStack, Double>) chances.toArray()[0]).getKey();
    }

    // -------- -------- -------- -------- -------- --------

    public static EnvoyChest get(Chest chest) {
        int x = chest.getX();
        int y = chest.getY();
        int z = chest.getZ();
        for (EnvoyChest ec : chests){
            if (ec.x == x && ec.y == y && ec.z == z)
                return ec;
        }
        return null;
    }

    // -------- -------- -------- -------- -------- --------

    public static void loadEnvoyChests() {
        YamlConfiguration conf = Configs.load("envoychests.yml", Main.getInstance());
        conf.getConfigurationSection("envoychests").getKeys(false).forEach( s -> {
            int x = conf.getInt("envoychests." + s + ".x");
            int y = conf.getInt("envoychests." + s + ".y");
            int z = conf.getInt("envoychests." + s + ".z");
            new EnvoyChest(x, y, z);
        } );
    }

    public static void loadEnvoyItems() {
        YamlConfiguration conf = Configs.load("envoyitems.yml", Main.getInstance());
        conf.getConfigurationSection("envoyitems").getKeys(false).forEach( s -> {
            ItemStack itemStack = conf.getItemStack("envoyitems." + s + ".item");
            double chance = conf.getDouble("envoyitems." + s + ".chance");
            Bukkit.getConsoleSender().sendMessage(itemStack.toString());
            envoyItems.put(itemStack, chance);
        } );
    }

    // -------- -------- -------- -------- -------- --------

    public static void registerItemStack(ItemStack itemStack, double chance) {
        if (envoyItems.containsKey(itemStack))
            envoyItems.replace(itemStack, chance);

        else {
            envoyItems.put(itemStack, chance);

            YamlConfiguration conf = Configs.load("envoyitems.yml", Main.getInstance());
            int id = conf.getInt("nextId");
            conf.set("envoyitems." + id + ".item", itemStack);
            conf.set("envoyitems." + id + ".chance", chance);
            Configs.save(conf, "envoyitems.yml");
        }
    }

    public static void registerChest(Chest chest) {
        YamlConfiguration conf = Configs.load("envoychests.yml", Main.getInstance());
        int nextId = conf.getInt("nextId");
        int x = chest.getX();
        int y = chest.getY();
        int z = chest.getZ();
        conf.set("envoychests." + nextId + ".x", x);
        conf.set("envoychests." + nextId + ".y", y);
        conf.set("envoychests." + nextId + ".z", z);
        conf.set("nextId", nextId + 1);
        Configs.save(conf, "envoychests.yml");
    }

}
