package pl.trollcraft.command.cells;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.cells.Cell;
import pl.trollcraft.obj.cells.PendingVisit;
import pl.trollcraft.util.ChatUtil;

public class VisitCellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jest dostepna jedynie dla graczy online."));
            return true;
        }

        Player player = (Player) sender;

        if (player.getWorld().getName().equals("envoy")){
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cNie mozesz odwiedzic celi gracza bedac na przemycie."));
            return true;
        }

        if (args.length == 1) {

            Player prisoner = Bukkit.getPlayer(args[0]);
            if (prisoner == null || !prisoner.isOnline()){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTen wiezien jest OFFLINE."));
                return true;
            }

            Cell cell = Cell.get(prisoner);
            if (cell == null){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTen wiezien nie posiada celi."));
                return true;
            }

            if (PendingVisit.isAlreadyVisiting(player, cell)){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cJuz poprosiles o pozwolenie na odwiedziny tej celi."));
                return true;
            }

            new PendingVisit(player, cell);
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Poprosiles o odwiedzenie celi."));
            ChatUtil.sendMessage(prisoner, ChatUtil.fixColor("&e" + prisoner.getName() + " &7prosi o pozwolenie na odwiedziny Twojej celi.\n" +
                    "&7Prosba przedawni sie za &e1 minute.\n&7Aby pozwolic - &e/pozwol"));
            return true;

        }
        else
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Uzycie: /odwiedz <wiezien>"));


        return false;
    }
}
