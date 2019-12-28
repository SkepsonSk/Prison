package pl.trollcraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pl.trollcraft.obj.PlayerMoveDetectedEvent;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.DelayedWarp;

public class MoveListener implements Listener {

    @EventHandler
    public void onMoveDetect (PlayerMoveDetectedEvent event) {

        Player player = event.getPlayer();

        if (DelayedWarp.hasAwaitingWarp(player)){
            DelayedWarp.cancel(player);
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cPoruszyles sie. Teleportacja anulowana."));
        }

    }

}
