package pl.trollcraft.rtp;

import org.bukkit.Location;
import org.bukkit.World;

public class Position {

    private int x;
    private int y;
    private int z;

    public Position(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location parseLocation(World world) {
        return new Location(world, x, y, z);
    }

}
