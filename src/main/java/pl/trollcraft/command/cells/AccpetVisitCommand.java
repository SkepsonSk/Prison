package pl.trollcraft.command.cells;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.cells.PendingVisit;
import pl.trollcraft.util.ChatUtil;

import java.util.ArrayList;

public class AccpetVisitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jest dostepna jedynie dla graczy online."));
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {

            ArrayList<PendingVisit> visits = PendingVisit.getPendingVisits(player);
            if (visits.isEmpty()){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Brak prosb odwiedzin."));
                return true;
            }
            else{
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Prosby odwiedzin:\n\n"));
                for (PendingVisit pv : visits)
                    ChatUtil.sendMessage(sender, ChatUtil.fixColor(" - &e" + pv.getVisitorName()));
                return true;
            }

        }
        else{

            Player visitor = Bukkit.getPlayer(args[0]);
            if (visitor == null || !visitor.isOnline()){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTen wiezien opuscil juz gre."));
                return true;
            }

            if (PendingVisit.accept(player, visitor)){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&aZaakceptowano prosbe odwiedzin tego wieznia."));
                return true;
            }
            else{
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak prosb od tego wieznia."));
                return true;
            }

        }

    }
}
