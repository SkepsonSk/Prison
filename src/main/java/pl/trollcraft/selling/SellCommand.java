package pl.trollcraft.selling;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.backpacks.Backpack;
import pl.trollcraft.obj.booster.Booster;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Utils;
import tesdev.Money.MoneyAPI;
import tesdev.Money.api.EconomyProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class SellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {

            if (!sender.hasPermission("prison.sellall")) {
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cAby moc sprzedawac komenda, musisz byc graczem VIP."));
                return true;
            }

            sellAll(player);

        }
        else if (args.length == 1){

            if (args[0].equalsIgnoreCase(Main.getSells().getString("pass")))
                sellAll(player);

        }

        return true;
    }

    private void sellAll(Player player) {

        Inventory inv = player.getInventory();
        Material m;
        double sum = 0, s;
        ArrayList<ItemStack> rem = new ArrayList<>();
        for (int i = 0 ; i < inv.getContents().length ; i++) {
            ItemStack is = inv.getContents()[i];
            if (is == null) continue;
            m = is.getType();
            s = SellingUtils.getPrice(m, is.getData().getData(), true);

            if (s == 0) continue;

            sum += s * is.getAmount();
            rem.add(is);
        }
        for (ItemStack is : rem)
            inv.remove(is);
        rem.clear();

        ArrayList<ItemStack> backpackItems = Backpack.getItemsInBackpack(player, true);
        if (backpackItems != null){
            for (ItemStack is : Backpack.getItemsInBackpack(player, true)){
                if (is == null || is.getType() == Material.AIR) continue;
                m = is.getType();
                s = SellingUtils.getPrice(m, is.getData().getData(), true);

                if (s == 0) continue;

                sum += s * is.getAmount();
                rem.add(is);
            }

            if (!rem.isEmpty())
                Backpack.tryRemoveItemsFromBackpacks(player, rem, false);
        }

        if (sum == 0){
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cBrak surowcow do sprzedania."));
            return;
        }

        double sumRounded = Utils.round(sum, 3);
        double bonus = Booster.getBonus(player);

        EconomyProfile.FastAccess.addMoney(player, sumRounded * bonus);

        if (bonus == 1)
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Sprzedano surowce.\n&a&l+" + sumRounded));
        else
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Sprzedano surowce.\n&a&l+" + sumRounded + " * " + bonus + ", czyli " + (sumRounded*bonus)));
    }

}
