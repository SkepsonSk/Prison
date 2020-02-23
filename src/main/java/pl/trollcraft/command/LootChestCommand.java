package pl.trollcraft.command;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;
import pl.trollcraft.lootchests.LootChest;
import pl.trollcraft.lootchests.LootItem;
import pl.trollcraft.util.ChatUtil;

import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class LootChestCommand implements CommandExecutor {

    private static final Random RANDOM = new Random();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cKomenda jedynie dla graczy online."));
            return true;
        }

        if (!sender.hasPermission("prison.lootchests.admin")){
            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cBrak uprawnien."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0){

        }
        else if (args[0].equalsIgnoreCase("create")) {

            if (args.length != 2){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Uzycie: &7/lc create <nazwa>"));
                return true;
            }

            Block b = player.getTargetBlock( (Set<Material>) null, 3 );
            if (!(b.getState() instanceof Chest)){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cTo nie skrzynia."));
                return true;
            }

            Chest chest = (Chest) b.getState();
            String name = args[1].replaceAll("&", "§");
            new LootChest(RANDOM.nextInt(1000000), name, chest, null, 0d, new ArrayList<>());

            ChatUtil.sendMessage(player, ChatUtil.fixColor("&aSkrzynia utworzona."));
            return true;
        }
        else if (args[0].equalsIgnoreCase("add")){

            if (args.length != 2) {
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Uzycie: &7/lc add <rzadkosc>"));
                return true;
            }

            Block b = player.getTargetBlock((Set<Material>) null, 3);
            if (!(b.getState() instanceof Chest)){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cTo nie skrzynia."));
                return true;
            }

            Chest chest = (Chest) b.getState();
            LootChest lootChest = LootChest.get(chest);

            if (lootChest == null){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTa skrzynia nie jest zarejestrowana."));
                return true;
            }

            ItemStack itemStack = player.getItemInHand();
            if (itemStack == null || itemStack.getType() == Material.AIR) {
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cZly przedmiot!"));
            }

            LootItem.Rarity rarity = LootItem.Rarity.valueOf(args[1].toUpperCase());
            lootChest.addLootItem(itemStack, rarity);

            ChatUtil.sendMessage(player, ChatUtil.fixColor("&aPrzedmiot zostal dodany."));
            return true;
        }
        else if (args[0].equalsIgnoreCase("key")){

            Block b = player.getTargetBlock((Set<Material>) null, 3);
            if (!(b.getState() instanceof Chest)){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cTo nie skrzynia."));
                return true;
            }

            Chest chest = (Chest) b.getState();
            LootChest lootChest = LootChest.get(chest);

            if (lootChest == null){
                ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTa skrzynia nie jest zarejestrowana."));
                return true;
            }

            ItemStack itemStack = player.getItemInHand();
            if (itemStack == null || itemStack.getType() == Material.AIR){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cZly przedmiot."));
                return true;
            }

            lootChest.setKey(itemStack);
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&aKlucz zostal ustawiony."));

            return true;

        }
        else if (args[0].equalsIgnoreCase("keyDropChance")) {

            if (args.length != 2){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Uzycie: &e/lc keydropchance <szansa>"));
                return true;
            }

            Block b = player.getTargetBlock((Set<Material>) null, 3);
            if (!(b.getState() instanceof Chest)){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cTo nie skrzynia."));
                return true;
            }

            Chest chest = (Chest) b.getState();
            LootChest lootChest = LootChest.get(chest);
            double chance = Double.parseDouble(args[1]);



        }
        else if (args[0].equalsIgnoreCase("save")) {

            Block b = player.getTargetBlock((Set<Material>) null, 3);
            if (!(b.getState() instanceof Chest)){
                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cTo nie skrzynia."));
                return true;
            }

            Chest chest = (Chest) b.getState();
            LootChest lootChest = LootChest.get(chest);

            if (lootChest == null){

                ChatUtil.sendMessage(player, ChatUtil.fixColor("&cTa skrzynia nie jest zarejestrowana!"));
                return true;

            }

            lootChest.save();

            Hologram holo = HologramsAPI.createHologram(Main.getInstance(), chest.getLocation().add(0, 2, 0));
            holo.appendItemLine(lootChest.getKey());
            holo.appendTextLine(lootChest.getName().replaceAll("_", " "));

            ChatUtil.sendMessage(player, ChatUtil.fixColor("&aZapisano skrzynie!"));
            return true;

        }

        return true;
    }
}
