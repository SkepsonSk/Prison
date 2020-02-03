package pl.trollcraft.command.warp;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.DelayedWarp;
import pl.trollcraft.util.GeneralUtil;

public class WarpCommand implements CommandExecutor {

    private final String WARP_FORB_WORL = "warp_forbidden_worlds";

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                    Main.getInstance().getConfig().getString("prefix") +
                    " &cKomenda dostepna jedynie dla gracza online."));
            return true;
        }

        Player player = (Player) sender;
        String world = player.getWorld().getName();

        if (GeneralUtil.isInArray(world, Main.getInstance().getConfig().getStringList(WARP_FORB_WORL))) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                            Main.getInstance().getConfig().getString("prefix") +
                            " &cTutaj WARPy sa zablokowane. Aby opuscic ten swiat, udaj sie na &e/spawn."));
            return true;
        }

        if (DelayedWarp.hasAwaitingWarp(player)){
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cMasz juz oczekujaca teleportacje."));
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

        if (warp.isLocked() && !player.hasPermission("prison.warps.unlocked")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor(
                    Main.getInstance().getConfig().get("prefixColor") +
                            Main.getInstance().getConfig().getString("prefix") +
                            "&cTen warp jest zablokowany."));
            return true;
        }

        warp.teleport(player);

        return true;
    }

}
