package pl.trollcraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.ChatUtil;

public class AutoSellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (!(sender instanceof Player))
            return false;

        if (!sender.hasPermission("prison.autosell")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cNiestety, ale dostep komendy &e/autosell &cposiadaja jedynie gracze &eVIP."));
            return true;
        }

        AutoSell.Operation oper = AutoSell.change((Player) sender);

        if (oper == AutoSell.Operation.ENABLED)
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7AutoSell - &a&lWlaczono."));
        else
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7AutoSell - &c&lWylaczono."));

        return true;
    }
}
