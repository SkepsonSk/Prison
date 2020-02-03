package pl.trollcraft.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.util.enchants.EnchantRegister;

public class BlastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        Player player = (Player) sender;

        ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE);
        itemStack.addEnchantment(EnchantRegister.BLAST_ENCHANTMENT, Integer.parseInt(args[0]));

        player.getInventory().addItem(itemStack);

        return false;
    }
}
