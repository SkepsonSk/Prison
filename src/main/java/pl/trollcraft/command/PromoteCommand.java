package pl.trollcraft.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.PrisonBlock;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.MinersManager;
import tesdev.Money.MoneyAPI;
import tesdev.Money.api.EconomyProfile;

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

        double price = next.getEnterPrice();
        int blocks = next.getEnterBlocksMined();
        double money = EconomyProfile.FastAccess.getMoney(player);
        int blocksMined = MinersManager.get(player);

        if (money >= price){

            if (blocksMined >= blocks){
                PrisonBlock.unlock(player, next);
                PrisonBlock.savePlayer(player);
                EconomyProfile.FastAccess.takeMoney(player, price);
                ChatUtil.sendMessage(player, "&aOdblokowano blok &e" + next.getName() + "!");
                String name = player.getName();
                for (Player p : Bukkit.getOnlinePlayers()){
                    if (p.getEntityId() != player.getEntityId()) {
                        ChatUtil.sendMessage(p, "");
                        ChatUtil.sendMessage(p, ChatUtil.fixColor("&7Wiezien &e" + name + " &7odblokowal block &e&l" + next.getName()));
                        ChatUtil.sendMessage(p, "");
                    }
                }
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
