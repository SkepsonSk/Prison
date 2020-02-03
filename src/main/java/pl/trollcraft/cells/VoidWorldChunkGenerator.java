package pl.trollcraft.cells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VoidWorldChunkGenerator extends ChunkGenerator {

    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new BlockPopulator[0]);
    }

    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    public byte[] generate(World world, Random rand, int chunkx, int chunkz) {
        return new byte[32768];
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 128, 0);
    }

    // -------- -------- --------- --------

    public static boolean exists() {
        return Bukkit.getWorld("cells") != null;
    }
    public static boolean envoyExists() { return Bukkit.getWorld("envoy") != null; }

    public static void generate() {
        WorldCreator creator = new WorldCreator("cells");
        creator.generateStructures(false);
        creator.generator(new VoidWorldChunkGenerator());
        creator.createWorld();
    }

    public static void generateEnvoy() {
        WorldCreator creator = new WorldCreator("envoy");
        creator.generateStructures(false);
        creator.generator(new VoidWorldChunkGenerator());
        creator.createWorld();
    }


}
