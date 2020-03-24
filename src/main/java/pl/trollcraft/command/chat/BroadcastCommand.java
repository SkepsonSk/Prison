package pl.trollcraft.command.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.util.ChatUtil;

public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("prison.admin")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak uprawnien."));
            return true;
        }

        if (args.length == 0) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Uzycie: &e/" + label + " (-p) <wiadomosc>"));
            return true;
        }

        int start = 0;
        boolean p = false; // Show nickname.

        if (args[0].startsWith("-")){

            if (args[0].length() > 1){
                start = 1;
                if (args[0].contains("p"))
                    p = true;

            }
            else {
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBledne argumenty."));
                return true;
            }

        }

        String prefix = "&e------------- &e&l[Ogloszenie] &e-------------";
        String suffix = "&e----------------------------------------------";

        StringBuilder msg = new StringBuilder();

        if (p)
            prefix = "&e----- &e&l[Ogloszenie " + sender.getName() + "] &e-----";

        for (int i = start ; i < args.length ; i++)
            msg.append(args[i] + " ");

        String message = ChatUtil.fixColor(msg.toString());

        for (Player player : Bukkit.getOnlinePlayers()) {
            ChatUtil.sendMessage(player, "");
            ChatUtil.sendMessage(player, prefix);
            ChatUtil.sendMessage(player, "");
            ChatUtil.sendMessage(player, message);
            ChatUtil.sendMessage(player, "");
            ChatUtil.sendMessage(player, suffix);
            ChatUtil.sendMessage(player, "");

        }

        return true;
    }
}
