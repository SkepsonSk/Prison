package pl.trollcraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.obj.warps.Warp;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.DelayedWarp;
import pl.trollcraft.util.GeneralUtil;

import java.util.concurrent.TimeUnit;

public class SpawnCommand implements CommandExecutor {

    private final String SPAWN = "spawn";
    private final String WARP_FORB_WORL = "warp_forbidden_worlds";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy online."));
        }

        Player player = (Player) sender;
        Warp spawn = Warp.get(SPAWN);

        if (DelayedWarp.hasAwaitingWarp(player)){
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cMasz juz oczekujaca teleportacje."));
            return true;
        }

        if (spawn == null){
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cSpawn nie zostal jeszcze ustawiony!"));
            return true;
        }

        String world = player.getWorld().getName();

        if (GeneralUtil.isInArray(world, Main.getInstance().getConfig().getStringList(WARP_FORB_WORL))){
            new DelayedWarp(player, TimeUnit.SECONDS.toMillis(5), spawn);
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Teleportacja nastapi za &e5 sekund. &7Nie ruszaj sie."));
        }
        else
            spawn.teleport(player);

        return true;
    }
}
