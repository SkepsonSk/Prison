package pl.trollcraft.command.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.util.ChatUtil;

public class MessageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 2){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Uzycie: /" + label + " <gracz> <wiadomosc>"));
            return true;
        }

        if (args[0].equals(sender.getName())){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cNie mozesz wyslac wiadomosci do samego siebie."));
            return true;
        }

        Player r = Bukkit.getPlayer(args[0]);

        if (r == null || !r.isOnline()){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cNie ma gracza na serwerze PRISON."));
            return true;
        }

        StringBuilder msg = new StringBuilder();
        for (int i = 1 ; i < args.length ; i++){
            msg.append(args[i]);
            msg.append(' ');
        }

        String message = msg.toString();
        ChatUtil.sendMessage(sender, ChatUtil.fixColor("&6[ Ja >>> " + r.getName() + " ] &f" + message));
        ChatUtil.sendMessage(r, ChatUtil.fixColor("&6[ " + sender.getName() + " >>> Ja ] &f" + message));

        return true;
    }
}
