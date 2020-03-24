package pl.trollcraft.lootchests;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
        private int index;
        private int indexLimit;

        public Session(GUI gui, Player player, LootChest lootChest, int animations){
            this.gui = gui;
            this.player = player;
            this.lootChest = lootChest;
            this.animations = animations;
            reward = getReward();
            lastItem = null;
            index = 0;
            indexLimit = lootChest.loot.values().size();
        }

        private Reward getReward() {
            double c = 0 + (100 - 0) * RAND.nextDouble();

            Debug.log("Szansa na drop wylosowana: " + c);

            ArrayList<Reward> rewards = null;
            ArrayList<LootItem.Rarity> rarities = new ArrayList<>(lootChest.loot.keySet());

            Collections.sort(rarities);
            Collections.reverse(rarities);

            for (LootItem.Rarity r : rarities){
                Debug.log(r.getName());
                if (c <= r.getChance()){
                    Debug.log("Selected: " + r.getName());
                    rewards = new ArrayList<>(lootChest.loot.get(r));
                    break;
                }
            }

            return rewards.get(RAND.nextInt(rewards.size()));
        }

        private void incrementIndex() {
            if (index == indexLimit) index = 0;
            else index++;
        }

    }

    private static Consumer<InventoryClickEvent> click = e -> e.setCancelled(true);

    private static BukkitTask runnable;
    private static ArrayList<Session> sessions = new ArrayList<>();
    private static ArrayList<LootChest> lootChests = new ArrayList<>();

    private int id;
    private String name;
    private Chest chest;
    private ItemStack key;
    private double keyDropChance;
    private Multimap<LootItem.Rarity, Reward> loot;

    // --------- -------- -------- -------- -------- --------

    public LootChest (int id, String name, Chest chest, ItemStack key, double keyDropChance, Multimap<LootItem.Rarity, Reward> loot) {
        this.id = id;
        this.name = name;
        this.chest = chest;
        this.key = key;
        this.keyDropChance = keyDropChance;
        this.loot = loot;
        lootChests.add(this);
    }

    // --------- -------- -------- -------- -------- --------

    public void addLootItem(ItemStack itemStack, LootItem.Rarity rarity) {
        loot.put(rarity, new ItemReward(itemStack));
    }

    public void addLootCommand(String name, String command, LootItem.Rarity rarity) {
        loot.put(rarity, new CommandReward(name, command));
    }

    public String getName() { return name; }
    public ItemStack getKey() { return key; }

    public void setKey(ItemStack key) { this.key = key; }

    public boolean keyMatches(ItemStack itemStack) {
        return itemStack.equals(key);
    }

    public ItemStack getItemStack(int ind) {
        int i = 0;
        for (Reward r : loot.values()) {
            if (i == ind) return r.getItem();
            i++;
        }
        return null;
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

        int i = 0;
        LootItem.Rarity rar;
        Reward rew;
        for (Map.Entry<LootItem.Rarity, Reward> e : loot.entries()){
            rar = e.getKey();
            rew = e.getValue();

            conf.set("lootchests." + id + ".lootitems." + i + ".rarity", rar.name().toUpperCase());

            if (rew instanceof ItemReward){
                conf.set("lootchests." + id + ".lootitems." + i + ".type", "itemstack");
                conf.set("lootchests." + id + ".lootitems." + i + ".itemstack", rew.getItem());
            }
            else if (rew instanceof CommandReward){
                conf.set("lootchests." + id + ".lootitems." + i + ".type", "command");
                conf.set("lootchests." + id + ".lootitems." + i + ".name",  ((CommandReward) rew).getName() );
                conf.set("lootchests." + id + ".lootitems." + i + ".command", ((CommandReward) rew).getCommand());
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

                        s.reward.give(s.player);
                        s.gui.addItem(13, s.reward.getItem(), event -> {
                            event.setCancelled(true);
                            s.player.closeInventory();
                        });

                        s.gui.addItem(15, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), click);
                        s.gui.addItem(11, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 4), click);

                        s.gui.update();

                        it.remove();
                        continue;
                    }

                    s.gui.addItem(13, s.lootChest.getItemStack(s.index), click);
                    s.incrementIndex();
                    s.gui.update();
                    s.animations--;
                }

                if (sessions.isEmpty()){
                    runnable.cancel();
                    runnable = null;
                }

            }

        }.runTaskTimer(Main.getInstance(), 5, 5);
    }

    // --------- -------- -------- -------- -------- --------

    public static LootChest get(int id) {
        for (LootChest lc : lootChests) {
            if (lc.id == id) return lc;
        }
        return null;
    }

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
            Multimap<LootItem.Rarity, Reward> loot = ArrayListMultimap.create();

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

                loot.put(rarity, reward);

            } );


            new LootChest(Integer.parseInt(id), name, chest, key, keyDropChance, loot);

            Hologram holo = HologramsAPI.createHologram(Main.getInstance(), chest.getLocation().add(0.5, 3, 0.5));
            holo.appendItemLine(key);
            holo.appendTextLine(name.replaceAll("_", " "));
        } );

    }

}
