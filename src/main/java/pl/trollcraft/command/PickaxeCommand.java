package pl.trollcraft.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.enchants.EnchantRegister;

import java.util.Arrays;

public class PickaxeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) {
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy online."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("prison.admin")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak uprawnien."));
            return true;
        }

        int lvl = 0;
        if (args.length == 1)
            lvl = Integer.parseInt(args[0]);

        ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE);
        itemStack.addEnchantment(EnchantRegister.BLAST_ENCHANTMENT, lvl);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(Arrays.asList(new String[] {
                ChatUtil.fixColor("&7Blast " + EnchantRegister.roman(lvl+1))
        }));
        itemStack.setItemMeta(itemMeta);

        player.getInventory().addItem(itemStack);

        return true;
    }
}
