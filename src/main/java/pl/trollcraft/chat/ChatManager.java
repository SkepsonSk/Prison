package pl.trollcraft.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.trollcraft.obj.PrisonBlock;
import pl.trollcraft.util.Debug;

public class ChatManager implements Listener {

    @EventHandler
    public void onChat (AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = ChatProcessor.deflood(event.getMessage());

        ChatProcessor.Response res = ChatProcessor.process(player, message);
        if (res != ChatProcessor.Response.OK){
            event.setCancelled(true);
            player.sendMessage(res.getMessage());
            return;
        }

        event.getRecipients().removeAll(ChatProfile.getChatOff());

        String prefix = PrisonBlock.getPlayerBlock(player).getName();
        String suffix = PlaceholderAPI.setBracketPlaceholders(player, "{luckperms_suffix}");
        String name = player.getName();

        String format = ChatColor.translateAlternateColorCodes('&', "&7&l[&a" + prefix + "&7&l] " + suffix + " " + name + ": "  + "%2$s");

        if (player.hasPermission("prison.vip")) {
            if (!player.hasPermission("prison.admin"))
                message = message.replaceAll("&c", "").replaceAll("&4", "");
            event.setMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        else
            event.setMessage(message);

        event.setFormat(format);
    }

}
