package pl.trollcraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.trollcraft.util.GuiUtils;

public class GuiCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player player = (Player) sender;
        Inventory inv = GuiUtils.gui.create(player);
        player.openInventory(inv);
        return true;
    }
}
