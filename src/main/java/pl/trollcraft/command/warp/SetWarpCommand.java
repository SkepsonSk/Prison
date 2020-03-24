package pl.trollcraft.command.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.obj.warps.Warp;
import pl.trollcraft.util.ChatUtil;

public class SetWarpCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cKomenda dostepna jedynie dla gracza online."));
            return true;
        }

        if (!sender.hasPermission("prison.setwarp")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cBrak uprawnien."));
            return true;
        }

        if (args.length == 1 || args.length > 2) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cUzycie: /setwarp <nazwa> <zablokowany*>"));
            return true;
        }

        Player player = (Player) sender;
        String name = args[0].toLowerCase();

        if (Warp.get(name) != null){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cWarp o takiej nazwie juz istnieje."));
            return true;
        }

        boolean locked = false;

        if (args.length == 2) {
            locked = Boolean.parseBoolean(args[1]);
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7(Blokada warp'u: &e" + locked + "&7)"));
        }

        new Warp(name, player.getLocation(), locked).save();
        ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                Main.getInstance().getConfig().get("prefixColor") +
                Main.getInstance().getConfig().getString("prefix") +
                " &aWarp utworzony (" + name + ")"));

        return true;
    }
}
