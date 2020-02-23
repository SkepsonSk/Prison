package pl.trollcraft.lootchests;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.gui.GUI;

import java.util.*;
import java.util.function.Consumer;

public class LootChest{

    private static final Random RAND = new Random();

    private class Session {

        private GUI gui;
        private LootChest lootChest;
        private int animations;
        private ItemStack reward;

        public Session(GUI gui, LootChest lootChest, int animations){
            this.gui = gui;
            this.lootChest = lootChest;
            this.animations = animations;
            reward = getReward();
        }

        private ItemStack getReward() {
            int chance = RAND.nextInt(100);
            for (LootItem li : lootChest.lootItems){
                if (chance <= li.getRarity().getChance()) return li.getItemStack();
            }
            return null;
        }

    }

    private static Consumer<InventoryClickEvent> click = e -> e.setCancelled(true);

    private static BukkitTask runnable;
    private static ArrayList<Session> sessions = new ArrayList<>();
    private static ArrayList<LootChest> lootChests = new ArrayList<>();
    private static ArrayList<KeyDropChance> chances = new ArrayList<>();

    private int id;
    private String name;
    private Chest chest;
    private ItemStack key;
    private double keyDropChance;
    private ArrayList<LootItem> lootItems;

    // --------- -------- -------- -------- -------- --------

    public LootChest (int id, String name, Chest chest, ItemStack key, double keyDropChance, ArrayList<LootItem> lootItems) {
        this.id = id;
        this.name = name;
        this.chest = chest;
        this.key = key;
        this.keyDropChance = keyDropChance;
        this.lootItems = lootItems;
        lootChests.add(this);
    }

    // --------- -------- -------- -------- -------- --------

    public void addLootItem(ItemStack itemStack, LootItem.Rarity rarity) {
        lootItems.add(new LootItem(itemStack, rarity));
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public ItemStack getKey() { return key; }
    public double getKeyDropChance() { return keyDropChance; }

    public void setKey(ItemStack key) { this.key = key; }
    public void setKeyDropChance(double keyDropChance) { this.keyDropChance = keyDropChance; }

    public boolean keyMatches(ItemStack itemStack) {
        return itemStack.equals(key);
    }

    public ItemStack getRandomItemStack() {
        ItemStack r = lootItems.get(RAND.nextInt(lootItems.size())).getItemStack();
        return r;
    }

    // --------- -------- -------- -------- -------- --------

    public void open(Player player) {
        GUI gui = new GUI(27, name.replaceAll("_", " "));
        sessions.add(new Session(gui, this, 15));
        gui.open(player);
        invokeAnimator();
    }

    // --------- -------- -------- -------- -------- --------

    public void save() {
        YamlConfiguration conf = Configs.load("lootchests.yml", Main.getInstance());

        if (conf.contains("lootchests." + id)) conf.set("lootchests." + id, null);

        conf.set("lootchests." + id + ".name", name);
        conf.set("lootchests." + id + ".x", chest.getX());
        conf.set("lootchests." + id + ".y", chest.getY());
        conf.set("lootchests." + id + ".z", chest.getZ());
        conf.set("lootchests." + id + ".key", key);
        conf.set("lootchests." + id + ".keydropchance", keyDropChance);

        LootItem item;
        for (int i = 0 ; i < lootItems.size() ; i++){

            item = lootItems.get(i);
            conf.set("lootchests." + id + ".lootitems." + i + ".itemStack", item.getItemStack());
            conf.set("lootchests." + id + ".lootitems." + i + ".rarity", item.getRarity().name().toUpperCase());

        }

        Configs.save(conf, "lootchests.yml");

    }

    // --------- -------- -------- -------- -------- --------

    public static void invokeAnimator() {
        if (runnable != null) return;

        runnable = new BukkitRunnable() {

            @Override
            public void run() {

                Iterator<Session> it = sessions.iterator();
                while (it.hasNext()){
                    Session s = it.next();

                    if (s.animations == 0) {
                        s.gui.addItem(13, s.reward, null);

                        s.gui.addItem(15, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), click);
                        s.gui.addItem(11, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), click);

                        s.gui.update();

                        it.remove();
                        continue;
                    }

                    s.gui.addItem(13, s.lootChest.getRandomItemStack(), click);
                    s.gui.update();
                    s.animations--;
                }

                if (sessions.isEmpty()){
                    runnable.cancel();
                    runnable = null;

                    Debug.log("LootChest Animator off.");
                }

            }

        }.runTaskTimer(Main.getInstance(), 5, 5);
    }

    // --------- -------- -------- -------- -------- --------

    public static ItemStack randomizeDrop() {
        int c = RAND.nextInt(100);
        for (KeyDropChance chance : chances) {
            if ( c <= chance.getChance()) return getKey(chance.getId());
        }
        return null;
    }

    public static ItemStack getKey(int id) {
        for (LootChest lc : lootChests) {
            if (lc.id == id) return lc.key;
        }
        return null;
    }

    // --------- -------- -------- -------- -------- --------

    public static LootChest get(Chest chest) {
        for (LootChest lc : lootChests) {
            if (lc.chest.getX() == chest.getX() &&
                lc.chest.getY() == chest.getY() &&
                lc.chest.getZ() == chest.getZ())
                return lc;
        }
        return null;
    }

    public static void load() {

        YamlConfiguration conf = Configs.load("lootchests.yml", Main.getInstance());
        conf.getConfigurationSection("lootchests").getKeys(false).forEach( id -> {

            String name = conf.getString("lootchests." + id + ".name");
            int x = conf.getInt("lootchests." + id + ".x");
            int y = conf.getInt("lootchests." + id + ".y");
            int z = conf.getInt("lootchests." + id + ".z");

            World world = Bukkit.getWorld("world");
            Block b = world.getBlockAt(x, y, z);
            if (!(b.getState() instanceof Chest)){
                Bukkit.getConsoleSender().sendMessage("Error, no chest at position for lootchest " + id + ".");
                return;
            }

            Chest chest = (Chest) b.getState();
            ItemStack key = conf.getItemStack("lootchests." + id + ".key");
            double keyDropChance = conf.getDouble("lootchests." + id + ".keydropchance");
            ArrayList<LootItem> lootItems = new ArrayList<>();

            conf.getConfigurationSection("lootchests." + id + ".lootitems").getKeys(false).forEach( ord -> {
                ItemStack is = conf.getItemStack("lootchests." + id + ".lootitems." + ord + ".itemStack");
                LootItem.Rarity rarity = LootItem.Rarity.valueOf(conf.getString("lootchests." + id + ".lootitems." + ord + ".rarity"));
                lootItems.add(new LootItem(is, rarity));
            } );

            chances.add(new KeyDropChance(new LootChest(Integer.parseInt(id), name, chest, key, keyDropChance, lootItems)));

        } );

        Collections.sort(chances);
    }

}
