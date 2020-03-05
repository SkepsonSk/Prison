package pl.trollcraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.PrisonBlock;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.MinersManager;
import tesdev.Money.MoneyAPI;

public class PromoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy online!"));
            return true;
        }

        Player player = (Player) sender;

        PrisonBlock pb = PrisonBlock.getPlayerBlock(player);
        PrisonBlock next = pb.getNext();

        if (next == null){
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Odblokowano juz kazdy blok. Brawo!"));
            return true;
        }

        MoneyAPI api = MoneyAPI.getInstance();
        double price = next.getEnterPrice();
        int blocks = next.getEnterBlocksMined();
        double money = api.getMoney(player);
        int blocksMined = MinersManager.get(player);


        if (money >= price){

            if (blocksMined >= blocks){
                PrisonBlock.unlock(player, next);
                PrisonBlock.savePlayer(player);
                ChatUtil.sendMessage(player, "&aOdblokowano blok &e" + next.getName() + "!");
                api.removeMoney(player, price);
            }
            else{
                int missing = blocks - blocksMined;
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cAby odblokowac blok &e" + next.getName() +
                        " &7musisz wykopac jeszcze &e" + missing + " blokow."));
                return true;
            }

        }
        else{
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cAby odblokowac blok &e" + next.getName() +
                    "&c potrzebujesz &e" + price + " &cmonet."));
        }

        return true;
    }
}
