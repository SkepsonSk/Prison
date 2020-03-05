package pl.trollcraft.rtp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;

import java.util.Random;

public class RandomTeleport {

    private static Random RANDOM = new Random();

    private static int centerX;
    private static int centerZ;
    private static int maxX;
    private static int maxZ;
    private static int minX;
    private static int minZ;

    public static void init() {
        YamlConfiguration conf = Configs.load("rtp.yml", Main.getInstance());
        centerX = conf.getInt("rtp.center.x");
        centerZ = conf.getInt("rtp.center.z");
        maxX = conf.getInt("rtp.x.max");
        minX = conf.getInt("rtp.x.min");
        maxZ = conf.getInt("rtp.z.max");
        minZ = conf.getInt("rtp.z.min");
    }

    public static void teleport(Player player, World world) {
        Location location = randomize(world);
        player.teleport(location);
    }

    public static Location randomize(World world){
        int x = RANDOM.nextInt(maxX - minX) + minX;
        int z = RANDOM.nextInt(maxZ - minZ) + minZ;
        int y = world.getHighestBlockYAt(x, z);

        if (world.getBlockAt(x, y, z).getType() == Material.WATER
            || world.getBlockAt(x, y, z).getType() == Material.STATIONARY_WATER)
            return randomize(world);
        else
            return new Location(world, x, y + 1, z);
    }

}
