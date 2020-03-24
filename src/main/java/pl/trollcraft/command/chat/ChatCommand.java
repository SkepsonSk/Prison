package pl.trollcraft.command.chat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.chat.ChatProcessor;
import pl.trollcraft.chat.ChatProfile;
import pl.trollcraft.util.ChatUtil;

public class ChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Zarzadzanie chat'em:"));
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&e/chat toggle - &7wlacza/wylacza chat,"));
            return true;
        }

        if (args[0].equalsIgnoreCase("gtoggle")) {

            if (!sender.hasPermission("prison.admin")){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak uprawnien."));
                return true;
            }

            ChatProcessor.switchChat();

            String message = "&7Administrator &awlaczyl chat.";
            if (!ChatProcessor.isChatEnabled())
                message = "&7Administrator &cwylaczyl chat.";

            message = ChatUtil.fixColor(message);

            for (Player p : Bukkit.getOnlinePlayers()) {
                ChatUtil.sendMessage(p, "");
                ChatUtil.sendMessage(p, ChatUtil.fixColor(message));
                ChatUtil.sendMessage(p, "");
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("toggle")) {

            if (!(sender instanceof Player)){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy online."));
                return true;
            }

            if (!sender.hasPermission("prison.vip")){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda ta dostepna jest dla graczy &eVIP i SVIP."));
                return true;
            }

            Player player = (Player) sender;
            ChatProfile profile = ChatProfile.get(player);

            boolean chat = !profile.hasChatEnabled();
            profile.setChatEnabled(chat);

            if (chat)
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&aWlaczono chat."));
            else
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cWylaczono chat."));

            return true;
        }

        return true;
    }
}
