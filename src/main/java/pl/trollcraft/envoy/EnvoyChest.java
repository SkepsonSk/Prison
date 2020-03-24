package pl.trollcraft.envoy;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.obj.warps.Warp;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.Debug;

import java.util.*;

public class EnvoyChest {

    private static long next;
    private static long stops;

    public static World ENVOY_WORLD;
    public static final Random RAND = new Random();

    private static ArrayList<EnvoyChest> chests = new ArrayList<>();
    private static Multimap<Double, ItemStack> loot = ArrayListMultimap.create();

    private int x, y, z;
    private boolean opened;
    private Hologram hologram;

    // -------- -------- -------- -------- -------- --------

    public EnvoyChest(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        opened = false;
        chests.add(this);
    }

    public boolean hasBeenOpened() { return opened; }
    public void setHasBeenOpened(boolean opened) {
        this.opened = opened;
        hologram.clearLines();

        if (opened)
            hologram.appendTextLine("§cZgarnieta...");

        else
            hologram.appendTextLine("§aZgarnij mnie!");

    }

    public void spawnItems(Chest chest) {
        Inventory inv = chest.getBlockInventory();
        inv.clear();
        int slot = RAND.nextInt(24);
        inv.setItem(slot, getRandomItemStack());
    }

    // -------- -------- -------- -------- -------- --------

    private static boolean envoy = false;
    public static boolean envoyOn () { return envoy; }

    public static long whenNext() { return next; }
    public static long whenStops() { return stops; }

    public static void startEnvoy() {
        if (ENVOY_WORLD == null) ENVOY_WORLD = Bukkit.getWorld("envoy");

        if (envoy) return;

        for (EnvoyChest e : chests) {

            if (e.hologram == null)
                e.hologram = HologramsAPI.createHologram(Main.getInstance(), new Location(ENVOY_WORLD, e.x, e.y, e.z).add(0.5, 3, 0.5));

            e.opened = false;
            e.hologram.clearLines();
            e.hologram.appendTextLine("§aZgarnij mnie!");
        }

        String message = ChatUtil.fixColor("&7Rozpoczyna sie zrzut! Chcesz zawalczyc o cenne lupy?\n&eWpisz komende /przemyt.\n&eZrzut potrwa 10 minut.");

        envoy = true;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("");
            ChatUtil.sendMessage(p, message);
            p.sendMessage("");
        }
        stops = System.currentTimeMillis() + 10L * 60L * 1000L;
    }

    public static void stopEnvoy() {
        if (!envoy) return;
        for (EnvoyChest e : chests) {

            if (e.hologram == null)
                e.hologram = HologramsAPI.createHologram(Main.getInstance(), new Location(ENVOY_WORLD, e.x, e.y, e.z).add(0.5, 3, 0.5));

            e.opened = false;
            e.hologram.clearLines();
            e.hologram.appendTextLine("§cOczekuje na przemyt...");
        }

        Warp spawn = Warp.get("spawn");
        for (Player p : Bukkit.getWorld("envoy").getPlayers())
            spawn.teleport(p);

        String message = ChatUtil.fixColor("&7Zrzut zakonczyl sie! Kolejny za &e20 minut!\n&7Aby dowiedziec sie ile czasu pozostalo, &euzyj /przemyt");
        envoy = false;

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("");
            ChatUtil.sendMessage(p, message);
            p.sendMessage("");
        }
        next = System.currentTimeMillis() + 20L * 60L * 1000L;
    }

    // -------- -------- -------- -------- -------- --------

    public static void init() {
        next = System.currentTimeMillis() + 20L * 60L * 1000L;
        new BukkitRunnable() {

            @Override
            public void run() {

                if (envoy){
                    if (System.currentTimeMillis() >= stops){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                stopEnvoy();
                            }
                        }.runTask(Main.getInstance());
                    }
                }
                else {
                    if (System.currentTimeMillis() >= next){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                startEnvoy();
                            }
                        }.runTask(Main.getInstance());
                    }
                }

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), 20, 20 * 10);
    }

    private static ItemStack getRandomItemStack() {
        double c = 0 + (100 - 0) * RAND.nextDouble();

        ArrayList<ItemStack> items = null;
        ArrayList<Double> chances = new ArrayList<>(loot.keySet());

        Collections.sort(chances);

        for (double chance : chances){

            Debug.log(chance);

            if (c <= chance){
                Debug.log("Selected: " + chance);
                items = new ArrayList<>(loot.get(chance));
                break;
            }
        }

        if (items == null)
            items = new ArrayList<>(loot.get(chances.get(chances.size() - 1)));

        return items.get(RAND.nextInt(items.size()));
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

    // -------- -------- -------- -------- -------- --------

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

    // -------- -------- -------- -------- -------- --------

    public static void registerItemStack(ItemStack itemStack, double chance) {
        loot.put(chance, itemStack);
        YamlConfiguration conf = Configs.load("envoyitems.yml", Main.getInstance());
        int id = conf.getInt("nextId");
        conf.set("envoyitems." + id + ".item", itemStack);
        conf.set("envoyitems." + id + ".chance", chance);
        conf.set("nextId", id + 1);
        Configs.save(conf, "envoyitems.yml");
    }

    public static void loadEnvoyItems() {
        Debug.log("LUKING");
        YamlConfiguration conf = Configs.load("envoyitems.yml", Main.getInstance());
        conf.getConfigurationSection("envoyitems").getKeys(false).forEach( s -> {
            ItemStack itemStack = conf.getItemStack("envoyitems." + s + ".item");
            double chance = conf.getDouble("envoyitems." + s + ".chance");
            loot.put(chance, itemStack);
            Debug.log(chance);
        } );

    }

}
