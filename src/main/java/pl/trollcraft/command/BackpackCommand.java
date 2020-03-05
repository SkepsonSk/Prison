package pl.trollcraft.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.backpacks.Backpack;
import pl.trollcraft.util.ChatUtil;

public class BackpackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("prison.admin")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak uprawnien."));
            return true;
        }

        if (args.length == 2){

            int size = Integer.parseInt(args[1]);

            if (size > 54){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cMaksymalny rozmiar plecaka to 54."));
                return true;
            }
            if (size % 9 != 0){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cLiczba slotow musi byc podzielna przez 9."));
                return true;
            }

            Player r = Bukkit.getPlayer(args[0]);
            if (r == null || !r.isOnline()){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak gracza na serwerze."));
                return true;
            }

            Backpack backpack = new Backpack(size);
            r.getInventory().addItem(backpack.getItemStack());

            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&aPrzekazano plecak graczowi."));

        }
        else
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Uzycie: &e/" + label + " <gracz> <slotow %9>"));

        return true;
    }
}
