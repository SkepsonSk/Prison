package pl.trollcraft.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.math.transform.AffineTransform;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;

public class SchematicUtils {

    public static EditSession pasteSchematic(File file, Location origin, int rotate){
        EditSession editSession = null;
        try {
            AffineTransform transform = new AffineTransform();
            transform.rotateZ(rotate);
            editSession = ClipboardFormats
                    .findByFile(file).load(file)
                    .paste(new BukkitWorld(origin.getWorld()), new Vector(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ()), false, false, transform);
        } catch (IOException e) { e.printStackTrace(); }
        return editSession;
    }

    public static EditSession pasteSchematic(File file, Location origin){
        EditSession editSession = null;
        try {
            editSession = ClipboardFormats
                    .findByFile(file).load(file)
                    .paste(new BukkitWorld(origin.getWorld()), new Vector(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ()));
        } catch (IOException e) { e.printStackTrace(); }
        return editSession;
    }

}
