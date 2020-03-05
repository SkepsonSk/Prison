package pl.trollcraft.command.boosters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.booster.PlayerBooster;
import pl.trollcraft.util.ChatUtil;

public class BoosterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jest jedynie dla graczy."));
            return true;
        }

        Player player = (Player) sender;
        PlayerBooster playerBooster = PlayerBooster.getBooster(player);

        if (playerBooster == null) {
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cNie posiadasz &ebooster'a."));
            return true;
        }

        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Posiadasz &eBOOSTER!\n&7(Bonus &ex" + playerBooster.getBonus() + " &7aktywny jeszcze przez &e" + playerBooster.getSeconds() + " sekund)"));

        return true;
    }

}