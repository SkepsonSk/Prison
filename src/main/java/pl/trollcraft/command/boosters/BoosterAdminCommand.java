package pl.trollcraft.command.boosters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.booster.PlayerBooster;
import pl.trollcraft.util.ChatUtil;

public class BoosterAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("prison.admin") || sender instanceof ConsoleCommandSender){

            if (args.length == 0) {

            }
            if (args[0].equalsIgnoreCase("add")) {

                if (args.length != 4) {
                    ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Uzycie: /boosteradmin add <gracz> <bonus> <sekundy>"));
                    return true;
                }

                Player boosted = Bukkit.getPlayer(args[1]);
                double bonus = Double.parseDouble(args[2]);
                int seconds = Integer.parseInt(args[3]);

                if (boosted != null && boosted.isOnline()) {

                    PlayerBooster active = PlayerBooster.getBooster(boosted);
                    if (active != null){
                        bonus = Math.max(bonus, active.getBonus());

                        if (active.getSeconds() > 0)
                            seconds += active.getSeconds();

                        active.setBonus(bonus);
                        active.setSeconds(seconds);

                        ChatUtil.sendMessage(boosted, ChatUtil.fixColor("&aTwoj &eBOOSTER zostal przedluzony!\n&7(bonus &ex" + bonus + " &7przez &e" + seconds + " sekund)"));
                    }
                    else{
                        new PlayerBooster(seconds, bonus, boosted);
                        ChatUtil.sendMessage(boosted, ChatUtil.fixColor("&aOtrzymujesz &eBOOSTER!\n&7(bonus &ex" + bonus + " &7przez &e" + seconds + " sekund)"));
                    }

                }
                else
                    PlayerBooster.setOfflineBooster(args[1], bonus, seconds);

            }

        }
        else
            ChatUtil.sendMessage(sender, "&cBrak uprawnien!");

        return true;
    }
}
