package pl.trollcraft.command.envoy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.envoy.EnvoyChest;
import pl.trollcraft.envoy.EnvoyItem;
import pl.trollcraft.util.ChatUtil;

import java.util.Set;

public class EnvoyAdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy online."));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("prison.envoy.admin")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak uprawnien."));
            return true;
        }

        if (args.length == 0) {
            ChatUtil.sendMessage(player, ChatUtil.fixColor(
                    "&7Prison - zrzuty:\n\n" +
                         "&e/zrzut item <szansa> - &7dodaje trzymany przedmiot.\n" +
                         "&e/zrzut chest - &7rejestruje skrzynie.\n" +
                         "&e/zrzut start - &7uruchamia zrzut.\n" +
                         "&e/zrzut stop - &7zatrzymuje obecny zrzut.\n\n" +
                         "&72020 SkepsonSk ElementalShards"));

            return true;
        }
        else if (args.length == 1){

            if (args[0].equalsIgnoreCase("tp")) {

                if (Bukkit.getWorld("envoy") == null) {
                    player.sendMessage("O KURWA");
                }

                player.teleport(new Location(Bukkit.getWorld("envoy"), 0, 90, 0));
            }
            else if (args[0].equalsIgnoreCase("start"))
                EnvoyChest.startEnvoy();

            else if (args[0].equalsIgnoreCase("stop"))
                EnvoyChest.stopEnvoy();

            else if (args[0].equalsIgnoreCase("chest")){

                Block b = player.getTargetBlock((Set<Material>) null, 3);
                if (!(b.getState() instanceof Chest)){
                    ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTo nie jest skrzynia!"));
                    return true;
                }

                Chest chest = (Chest) b.getState();
                EnvoyChest.registerChest(chest);

                ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Skrzynie &ezarejestrowano."));
            }

            return true;

        }
        else if (args.length == 2) {

            if (args[0].equalsIgnoreCase("item")){
                ItemStack itemStack = player.getItemInHand();
                double chance = Double.parseDouble(args[1]);

                EnvoyItem.registerItemStack(itemStack, chance);
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Przedmiot &ezarejestrowano."));
            }

        }
        else
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cBledna liczba argumentow!"));

        return true;
    }
}
