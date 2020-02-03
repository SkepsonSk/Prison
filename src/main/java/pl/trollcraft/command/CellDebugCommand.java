package pl.trollcraft.command;

import com.sk89q.worldedit.EditSession;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.trollcraft.Main;
import pl.trollcraft.obj.cells.Cell;
import pl.trollcraft.obj.cells.CellNode;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.SchematicUtils;

import java.io.File;

public class CellDebugCommand implements CommandExecutor {

    private static World CELLS_WORLD = Bukkit.getWorld("cells");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (args.length == 0){
            for (CellNode cn : CellNode.getCellNodes())
                sender.sendMessage(cn.toString());
            sender.sendMessage("");
        }
        else if (args[0].equalsIgnoreCase("gen")) {

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

            EditSession editSession = SchematicUtils.pasteSchematic(cellFile, cellPos);
            editSession.addNotifyTask( () -> {
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Jestes przenoszony do swojej celi."));
            } );

            if (cellNode != null)
                cellNode.createCorridors(CELLS_WORLD);


            CellNode.saveAll();

        }

        return true;
    }
}
