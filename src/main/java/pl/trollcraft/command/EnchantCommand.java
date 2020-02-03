package pl.trollcraft.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.enchanting.EnchantGui;
import pl.trollcraft.util.ChatUtil;

public class EnchantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda dostepna jest jedynie dla graczy online."));
            return true;
        }

        Player player = (Player) sender;

        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cMusisz trzymac narzedzie w reku."));
            return true;
        }

        EnchantGui.open(player);

        return true;
    }
}
