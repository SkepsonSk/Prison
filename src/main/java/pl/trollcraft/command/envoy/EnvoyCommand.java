package pl.trollcraft.command.envoy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.envoy.EnvoyChest;
import pl.trollcraft.rtp.RandomTeleport;
import pl.trollcraft.util.ChatUtil;

public class EnvoyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (EnvoyChest.envoyOn()){

            Player player = (Player) sender;

            if (player.getWorld().getName().equals("envoy")){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cJestes juz na przemycie."));
                return true;
            }

            long stops = EnvoyChest.whenStops() - System.currentTimeMillis();
            long m = stops / 1000 / 60;
            long s = stops / 1000 % 60;
            String sec = String.valueOf(s);
            if (s < 10) sec = "0" + sec;

            player.setAllowFlight(false);
            player.setFlying(false);

            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Przenoszenie na przemyt...\n&eWyszukiwanie miejsca...\n&7Zrzut potrwa jeszcze &e" + m + ":" + sec));

            RandomTeleport.teleport(player, Bukkit.getWorld("envoy"));
        }
        else{
            long next = EnvoyChest.whenNext() - System.currentTimeMillis();
            long m = next / 1000 / 60;
            long s = next / 1000 % 60;
            String sec = String.valueOf(s);
            if (s < 10) sec = "0" + sec;

            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Nastepny zrzut rozpocznie sie za: &e" + m + ":" + sec));
            return true;
        }

        return true;

    }
}
