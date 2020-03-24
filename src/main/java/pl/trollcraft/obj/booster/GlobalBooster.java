package pl.trollcraft.obj.booster;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.trollcraft.util.ChatUtil;

public class GlobalBooster extends Booster {

    public GlobalBooster(int seconds, double bonus) {
        super(seconds, bonus);
        globalBoosters.add(this);
    }

    @Override
    public void end() {
        globalBoosters.remove(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Globalny Booster pieniezny wyczerpal sie..."));
        }
    }
}
