package pl.trollcraft.lootchests;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
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
import pl.trollcraft.lootchests.rewards.CommandReward;
import pl.trollcraft.lootchests.rewards.ItemReward;
import pl.trollcraft.lootchests.rewards.Reward;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.gui.GUI;

import java.util.*;
import java.util.function.Consumer;

public class LootChest{

    private static final Random RAND = new Random();

    private class Session {

        private GUI gui;
        private Player player;
        private LootChest lootChest;
        private int animations;
        private Reward reward;
        private ItemStack lastItem;

        public Session(GUI gui, Player player, LootChest lootChest, int animations){
            this.gui = gui;
            this.player = player;
            this.lootChest = lootChest;
            this.animations = animations;
            reward = getReward();
            lastItem = null;
        }

        private Reward getReward() {
            double c = 0 + (100 - 0) * RAND.nextDouble();
            for (LootItem li : lootChest.lootItems){
                if (c <= li.getChance())
                    return li.getReward();
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
        lootItems.add(new LootItem(new ItemReward(itemStack), rarity));
    }

    public void addLootCommand(String name, String command, LootItem.Rarity rarity) {
        lootItems.add(new LootItem(new CommandReward(name, command), rarity));
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

    public ItemStack getRandomItemStack(ItemStack last) {
        ItemStack r = lootItems.get(RAND.nextInt(lootItems.size())).getReward().getItem();
        while (r.equals(last))
            r = lootItems.get(RAND.nextInt(lootItems.size())).getReward().getItem();
        return r;
    }

    // --------- -------- -------- -------- -------- --------

    public void open(Player player) {
        GUI gui = new GUI(27, name.replaceAll("_", " "));
        sessions.add(new Session(gui, player, this, 15));
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
            conf.set("lootchests." + id + ".lootitems." + i + ".rarity", item.getRarity().name().toUpperCase());

            if (item.getReward() instanceof ItemReward){
                conf.set("lootchests." + id + ".lootitems." + i + ".type", "itemstack");
                conf.set("lootchests." + id + ".lootitems." + i + ".itemstack", item.getReward().getItem());
            }
            else if (item.getReward() instanceof CommandReward){
                conf.set("lootchests." + id + ".lootitems." + i + ".type", "command");
                conf.set("lootchests." + id + ".lootitems." + i + ".name",  ((CommandReward) item.getReward()).getName() );
                conf.set("lootchests." + id + ".lootitems." + i + ".command", ((CommandReward) item.getReward()).getCommand());
            }

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

                        if (s.reward instanceof CommandReward){

                            s.gui.addItem(13, s.reward.getItem(), event -> {
                                event.setCancelled(true);
                                s.reward.give(s.player);
                                s.player.closeInventory();
                            });

                        }
                        else if (s.reward instanceof ItemReward){
                            s.gui.addItem(13, s.reward.getItem(), event -> {
                                event.setCancelled(true);
                                s.reward.give(s.player);
                                s.player.closeInventory();
                            });
                        }

                        s.gui.addItem(15, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), click);
                        s.gui.addItem(11, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), click);

                        s.gui.update();

                        it.remove();
                        continue;
                    }

                    s.gui.addItem(13, s.lootChest.getRandomItemStack(s.lastItem), click);
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

            String name = conf.getString("lootchests." + id + ".name").replaceAll("&", "§");
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

                LootItem.Rarity rarity = LootItem.Rarity.valueOf(conf.getString("lootchests." + id + ".lootitems." + ord + ".rarity"));
                String type = conf.getString("lootchests." + id + ".lootitems." + ord + ".type");

                Reward reward = null;
                if (type.equals("itemstack")){
                    reward = new ItemReward(conf.getItemStack("lootchests." + id + ".lootitems." + ord + ".itemstack"));
                }
                else if (type.equals("command")){
                    String commandName = conf.getString("lootchests." + id + ".lootitems." + ord + ".name").replaceAll("_", " ");
                    String command = conf.getString("lootchests." + id + ".lootitems." + ord + ".command");
                    reward = new CommandReward(commandName, command);
                }

                lootItems.add(new LootItem(reward, rarity));

            } );

            Collections.sort(lootItems);

            chances.add(new KeyDropChance(new LootChest(Integer.parseInt(id), name, chest, key, keyDropChance, lootItems)));

            Hologram holo = HologramsAPI.createHologram(Main.getInstance(), chest.getLocation().add(0.5, 3, 0.5));
            holo.appendItemLine(key);
            holo.appendTextLine(name.replaceAll("_", " "));
        } );

        Collections.sort(chances);

    }

}
