package pl.trollcraft.util;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatUtil {

    public static String bcColor(String text) {
        if (text == null) {
            return "";
        }
        return text = text.replace("title", "").replace("chat", "").replace("actionbar", "").replace(">>", "�").replace("&", "�");
    }

    public static void giveItemsToAllPlayers(ItemStack... items) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            giveItems(p, items);
        }
    }

    public static boolean sendMessage(Collection<? extends CommandSender> collection, String message) {
        for (CommandSender cs : collection) {
            sendMessage(cs, message);
        }
        return true;
    }


    public static String locToString(Location loc) { return String.valueOf(String.valueOf(String.valueOf(loc.getX()))) + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch(); }


    public static Location locFromString(String str) {
        String[] str2loc = str.split(":");
        Location loc = new Location((World) Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        loc.setX(Double.parseDouble(str2loc[0]));
        loc.setY(Double.parseDouble(str2loc[1]));
        loc.setZ(Double.parseDouble(str2loc[2]));
        loc.setYaw(Float.parseFloat(str2loc[3]));
        loc.setPitch(Float.parseFloat(str2loc[4]));
        return loc;
    }

    public static String locToString(double x, double y, double z) { return String.valueOf(String.valueOf(String.valueOf(x))) + ":" + y + ":" + z + ":" + 0.0F + ":" + 0.0F; }

    public static boolean isInteger(String string) { return Pattern.matches("-?[0-9]+", string.subSequence(0, string.length())); }

    public static boolean sendMessage(Collection<? extends CommandSender> collection, String message, String permission) {
        for (CommandSender cs : collection) {
            sendMessage(cs, message, permission);
        }
        return true;
    }

    public static double round(double value, int decimals) {
        double p = Math.pow(10.0D, decimals);
        return Math.round(value * p) / p;
    }

    public static boolean sendMessage(CommandSender sender, String message, String permission) {
        if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
            sendMessage(sender, message);
        }
        return (permission != null && permission != "" && sender.hasPermission(permission) && sendMessage(sender, message));
    }

    public static boolean sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            if (message != null || message != "") {
                sender.sendMessage(fixColor(message));
            }
        } else {

            sender.sendMessage(ChatColor.stripColor(fixColor(message)));
        }
        return true;
    }


    public static String fixColor(String text) {
        if (text == null) {
            return "";
        }
        return text = text.replace("&", "§");
    }

    public static void giveItems(Player p, ItemStack... items) {
        PlayerInventory playerInventory = p.getInventory();
        HashMap<Integer, ItemStack> notStored = playerInventory.addItem(items);
        for (Map.Entry<Integer, ItemStack> e : notStored.entrySet()) {
            p.getWorld().dropItemNaturally(p.getLocation(), e.getValue());
        }
    }

    public static void sendMessageToAllAdmins(String msg, String permission) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission(permission)) {
                sendMessage(p, msg);
            }
        }
    }


}
