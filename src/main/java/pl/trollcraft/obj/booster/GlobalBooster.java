package pl.trollcraft.obj.booster;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.trollcraft.util.ChatUtil;

public class GlobalBooster extends Booster {

    public GlobalBooster(int seconds, double bonus) {
        super(seconds, bonus);
        Booster.globalBoosters.add(this);
    }

    @Override
    public void end() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Globalny Booster pieniezny wyczerpal sie..."));
        }
    }
}
