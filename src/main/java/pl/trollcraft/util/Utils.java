package pl.trollcraft.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.obj.PrisonBlock;
import tesdev.Money.MoneyAPI;
import tesdev.Money.api.EconomyProfile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.TreeMap;

public class Utils {

    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }

    public static int fromRoman(String r) {
        if (r.equals("I")) return 1;
        else if (r.equals("II")) return 2;
        else if (r.equals("III")) return 3;
        else if (r.equals("IV")) return 4;
        else if (r.equals("V")) return 5;
        else if (r.equals("VI")) return 6;

        return 0;
    }

    public static boolean stringEqual(String a, String b) {
        if (a.length() != b.length()) return false;
        for (int i = 0 ; i < a.length() ; i++) {
            if (a.charAt(i) != b.charAt(i)) return false;
        }
        return true;
    }

    public static ItemStack deserialize(YamlConfiguration conf, String path) {

        Material type = Material.getMaterial(conf.getString(path + ".type"));
        byte data = 0;

        if (conf.contains(path + ".data"))
            data = Byte.parseByte(conf.getString(path + ".data"));

        int amount = 1;
        if (conf.contains(path + ".amount"))
            amount = conf.getInt(path + ".amount");

        String name = null;
        List<String> lore = null;
        if (conf.contains(path + ".name"))
            name = conf.getString(path + ".name");
        if (conf.contains(path + ".lore"))
            lore = conf.getStringList(path + ".lore");

        ItemStack itemStack = new ItemStack(type, amount, data);

        if (conf.contains(path + ".enchantments")){
            conf.getStringList(path + ".enchantments").forEach( e -> {
                String[] enchData = e.split(":");
                Enchantment enchantment = Enchantment.getByName(enchData[0]);
                int level = Integer.parseInt(enchData[1]);
                itemStack.addEnchantment(enchantment, level);
            } );
        }

        if (name != null || lore != null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (name != null)
                itemMeta.setDisplayName(name);
            if (lore != null)
                itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void send(String text, EnumWrappers.TitleAction titleAction, int f, int d, int o, Player player) {

        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer pc = pm.createPacket(PacketType.Play.Server.TITLE);
        pc.getModifier().writeDefaults();
        pc.getTitleActions().write(0, titleAction);
        pc.getChatComponents().write(0, WrappedChatComponent.fromText(ChatUtil.fixColor(text)));
        pc.getIntegers().write(0, f);
        pc.getIntegers().write(1, d);
        pc.getIntegers().write(2, o);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, pc);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet.", e);
        }

    }

    public static double getPercentageToPromotionBlocks(Player player) {
        PrisonBlock block = PrisonBlock.getPlayerBlock(player).getNext();
        double blocksRequired = (double) block.getEnterBlocksMined();
        double mined = (double) MinersManager.get(player);
        return round(mined / blocksRequired * 100, 2);
    }

    public static double getPercentageToPromotionMoney(Player player) {
        PrisonBlock block = PrisonBlock.getPlayerBlock(player).getNext();
        double moneyRequired = block.getEnterPrice();
        double money = EconomyProfile.FastAccess.getMoney(player);
        return round(money / moneyRequired * 100, 2);
    }

}
