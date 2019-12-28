package pl.trollcraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;

public class FlyCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String lab, String[] args) {
        Player p = (Player)sender;
        if (!p.hasPermission("prison.fly")) {
            p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefix") + "&8>> &cNie dla psa kielbasa!"));
            return false;
        }
        if (args.length == 0 &&
                p.getAllowFlight()) {
            p.setAllowFlight(false);
            p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefix") + "&8>> &8[FLY] &aOff"));
            return true;
        }
        p.setAllowFlight(true);
        p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefix") + "&8>> &8[FLY] &aOn"));
        return true;
    }

}
