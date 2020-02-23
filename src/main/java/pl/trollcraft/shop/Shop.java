package pl.trollcraft.shop;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.Utils;
import pl.trollcraft.util.gui.GUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Shop {

    private static String globalName;
    private static int globalSlots;

    private static HashMap<Integer, Shop> shops = new HashMap<>();
    private static HashMap<Shop, Player> openedShops = new HashMap<>();

    private String name;
    private String description;
    private int slots;
    private ItemStack icon;
    private HashMap<Integer, Product> items;

    public Shop (String name, String description, int slots, ItemStack icon, HashMap<Integer, Product> items){
        this.name = name;
        this.description = description;
        this.slots = slots;
        this.icon = icon;
        this.items = items;
    }

    private void addProduct(int slot, ItemStack icon, ItemStack itemStack, double price){
        Product product = new Product(icon, itemStack, price);
        items.put(slot, product);
    }

    public void register(int slot) {
        shops.put(slot, this);
    }

    // -------- -------- -------- -------- -------- --------

    public void open(Player player) {
        GUI gui = new GUI(slots, name);

        items.forEach( (slot, product) -> {

            Consumer<InventoryClickEvent> buy = event -> {
                event.setCancelled(true);
                if (product.canBuy(player))
                    product.buy(player);
                else
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&cNie mozesz zakupic."));
            };

            ItemStack itemStack = product.getIcon().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.getLore() == null){
                List<String> lore = Arrays.asList( new String[] {"", ChatUtil.fixColor("&7Cena: &e" + product.getPrice())} );
                itemMeta.setLore(lore);
            }
            else{
                List<String> lore = itemMeta.getLore();
                lore.add("");
                lore.add(ChatUtil.fixColor("&7Cena: &e" + product.getPrice()));
                itemMeta.setLore(lore);
            }
            itemStack.setItemMeta(itemMeta);

            gui.addItem(slot, itemStack, buy);
        } );

        gui.open(player);
    }

    public static void openGlobal(Player player) {
        GUI gui = new GUI(globalSlots, globalName);
        shops.forEach( (slot, shop) -> {

            Consumer<InventoryClickEvent> open = event -> {
                event.setCancelled(true);
                shop.open(player);
            };

            ItemStack icon = shop.icon;
            ItemMeta itemMeta = icon.getItemMeta();

            if (shop.name != null)
                itemMeta.setDisplayName(shop.name);

            icon.setItemMeta(itemMeta);

            gui.addItem(slot, icon, open);
        } );

        gui.open(player);
    }

    // -------- -------- -------- -------- -------- --------

    public static void load() {
        YamlConfiguration conf = Configs.load("shops.yml", Main.getInstance());

        globalName = ChatUtil.fixColor(conf.getString("globalName"));
        globalSlots = conf.getInt("globalSlots");

        conf.getConfigurationSection("shops").getKeys(false).forEach( slot -> {
            String name = ChatUtil.fixColor(conf.getString("shops." + slot + ".name"));
            String description = conf.getString("shops." + slot + ".description");
            int slots = conf.getInt("shops." + slot + ".slots");
            ItemStack icon = Utils.deserialize(conf, "shops." + slot + ".icon");

            Shop shop = new Shop(name, description, slots, icon, new HashMap<>());

            conf.getConfigurationSection("shops." + slot + ".products").getKeys(false).forEach( s -> {
                ItemStack pIcon = Utils.deserialize(conf, "shops." + slot + ".products." + s + ".icon");
                ItemStack pItemStack = Utils.deserialize(conf, "shops." + slot + ".products." + s + ".itemStack");
                double pPrice = conf.getDouble("shops." + slot + ".products." + s + ".price");

                shop.addProduct(Integer.parseInt(s), pIcon, pItemStack, pPrice);
            } );

            shop.register(Integer.parseInt(slot));
        } );
    }

}
