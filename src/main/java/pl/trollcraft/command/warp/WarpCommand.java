package pl.trollcraft.command.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.util.ChatUtil;

public class WarpCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cKomenda dostepna jedynie dla gracza online."));
            return true;
        }

        if (args.length != 1) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cUzycie: /setwarp <nazwa>"));
            return true;
        }

        String name = args[0].toLowerCase();
        Warp warp = Warp.get(name);

        if (Warp.get(name) == null){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    "&cNie ma takiego warpa."));
            return true;
        }

        if (!sender.hasPermission("prison.warp." + name)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cBrak uprawnien."));
            return true;
        }

        Player player = (Player) sender;
        warp.teleport(player);

        return false;
    }

}
