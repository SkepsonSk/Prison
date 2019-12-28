package pl.trollcraft.util;

import org.bukkit.Location;

import java.util.Collection;

public class GeneralUtil {

    public static boolean isInArray(String a, Collection<? extends String> array) {
        for (String s : array)
            if (s.equals(a)) return true;
        return false;
    }

    public static int calcMove(Location loc){
        return loc.getBlockX() + loc.getBlockY() + loc.getBlockZ();
    }

}
