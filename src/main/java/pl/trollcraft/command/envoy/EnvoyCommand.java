package pl.trollcraft.command.envoy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.trollcraft.envoy.EnvoyChest;
import pl.trollcraft.util.ChatUtil;

public class EnvoyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (EnvoyChest.envoyOn()){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Zrzut wlasnie trwa!"));
            return true;
        }

        long next = EnvoyChest.whenNext() - System.currentTimeMillis();
        long m = next / 1000 / 60;
        long s = next / 1000 % 60;
        ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Nastepny zrzut rozpocznie sie za: &e" + m + ":" + s));
        return true;
    }
}
