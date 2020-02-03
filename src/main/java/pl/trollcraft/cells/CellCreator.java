package pl.trollcraft.cells;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.obj.cells.Cell;
import pl.trollcraft.obj.cells.CellNode;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CellCreator {

    private static World CELLS_WORLD = Bukkit.getWorld("cells");

    public static void create(Player player) {

        File cellFile = new File(Main.getInstance().getDataFolder(), "cell.schematic");
        Location cellPos;

        CellNode cellNode = null;
        if (CellNode.noCells()){
            new CellNode(0, 0);
            cellPos = new Location(CELLS_WORLD, 0, 90, 0);
        }
        else {
            int[] pos = CellNode.getPositionForNewCell();
            cellNode = new CellNode(pos[0], pos[1]);
            cellNode.hookConnections();
            cellNode.updateConnected();
            cellPos = new Location(CELLS_WORLD, pos[0], 90, pos[1]);
        }

        EditSession editSession = pasteSchematic(cellFile, cellPos);
        editSession.addNotifyTask( () -> {
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Jestes przenoszony do swojej celi."));
            player.teleport(cellPos);
        } );

        if (cellNode != null)
            cellNode.createCorridors(CELLS_WORLD);

        Cell cell = new Cell(player, cellPos, new ArrayList<>());
        cell.save();

        CellNode.saveAll();
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
