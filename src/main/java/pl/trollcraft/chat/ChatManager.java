package pl.trollcraft.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.trollcraft.obj.PrisonBlock;

public class ChatManager implements Listener {

    @EventHandler
    public void onChat (AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String prefix = PrisonBlock.getPlayerBlock(player).getName();
        String suffix = PlaceholderAPI.setBracketPlaceholders(player, "{luckperms_suffix}");
        String name = player.getName();

        String format = ChatColor.translateAlternateColorCodes('&', "&7&l[&a" + prefix + "&7&l] " + suffix + " " + name + ": "  + "%2$s");

        event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        event.setFormat(format);
    }

}
