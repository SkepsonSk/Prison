package pl.trollcraft.selling;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;

public class SellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {

            if (!sender.hasPermission("prison.sell")) {
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cAby moc sprzedawac komenda, musisz byc graczem VIP."));
                return true;
            }

            sellAll(player);

        }
        else if (args.length == 1){

            if (args[0].equalsIgnoreCase(Main.getSells().getString("pass"))) {
                sellAll(player);
            }

        }

        return true;
    }

    private void sellAll(Player player) {
        Inventory inv = player.getInventory();
        Material m;
        double sum = 0;
        for (ItemStack is : inv.getContents()) {
            if (is == null) continue;
            m = is.getType();
            player.sendMessage(m.ordinal() + "");
            if (SellingUtils.hasPrice(m)) sum += SellingUtils.getPrice(m) * is.getAmount();
        }
        player.sendMessage(String.valueOf(sum));

    }

}
