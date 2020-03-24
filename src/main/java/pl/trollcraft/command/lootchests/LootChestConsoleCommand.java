package pl.trollcraft.command.lootchests;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.lootchests.LootChest;
import pl.trollcraft.util.ChatUtil;

public class LootChestConsoleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof ConsoleCommandSender || sender.hasPermission("prison.admin"))){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak uprawnien!"));
            return true;
        }

        if (args.length != 3){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&eUzycie: &7/lck <skrzynia> <ilosc> <gracz>"));
            return true;
        }

        LootChest lootChest = LootChest.get(Integer.parseInt(args[0]));
        int amount = Integer.parseInt(args[1]);
        Player player = Bukkit.getPlayer(args[2]);

        if (lootChest == null){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cSkrzynia o podanym id nie istnieje."));
            return true;
        }

        if (player == null || !player.isOnline()){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cGracz o podanym nick'u nie istnieje."));
            return true;
        }

        ItemStack keys = lootChest.getKey();
        keys.setAmount(amount);
        player.getInventory().addItem(keys);

        ChatUtil.sendMessage(sender, ChatUtil.fixColor("&aPodarowano graczowi klucze."));
        ChatUtil.sendMessage(sender, ChatUtil.fixColor("&aOtrzymales pare kluczy do skrzyn, &emilego loot'owania!"));

        return true;
    }
}
