package pl.trollcraft.command.cells;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.cells.CellCreator;
import pl.trollcraft.obj.cells.Cell;
import pl.trollcraft.util.ChatUtil;

public class CellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTa komenda jest jedynie dla graczy online."));
            return true;
        }

        Player player = (Player) sender;

        if (player.getWorld().getName().equals("envoy")){
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cPodczas zrzutu nie mozesz uciec do celi!\n&7Aby udac sie do celi, idz wpierw na &e/spawn."));
            return true;
        }

        if (args.length == 0) {

            Cell cell = Cell.get(player);
            if (cell == null) {
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Przygotowywanie celi..."));
                CellCreator.create(player);
            }
            else {
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Jestes przenoszony do celi."));
                cell.teleport();
            }

        }

        return true;
    }
}
