package pl.trollcraft.command.anvil;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.util.ChatUtil;
import tesdev.Money.api.EconomyProfile;

public class RenameItemStackCommand implements CommandExecutor {

    private static final double PRICE_DEF = 100;
    private static final double PRICE_VIP = 50;
    private static final double PRICE_SVIP = 10;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy online."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&7Uzycie: &e/nazwa <nazwa>\n&7(zmieniana jest nazwa trzymanego przedmiotu)"));
            return true;
        }

        ItemStack item = player.getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cZly przedmiot."));
            return true;
        }

        double price = PRICE_DEF;
        if (player.hasPermission("prison.svip"))
            price = PRICE_SVIP;
        else if (player.hasPermission("prison.vip"))
            price = PRICE_VIP;

        if (EconomyProfile.FastAccess.getMoney(player) >= price){
            EconomyProfile.FastAccess.takeMoney(player, price);

            StringBuilder name = new StringBuilder();
            for (int i = 0 ; i < args.length ; i++){
                name.append(args[i]);
                name.append(' ');
            }

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatUtil.fixColor(name.toString()));
            item.setItemMeta(itemMeta);

            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&aNazwa przedmiotu zostala zmieniona.\n&c&l-" + price));

        }
        else{
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cZmiana nazwy kosztuje &e" + price + " monet."));
            return true;
        }

        return true;
    }
}
