package pl.trollcraft.command;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.util.ChatUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class KitCommand implements CommandExecutor {

    String[] kity = new String[] {"Start", "VIP", "SVIP"};

    public HashMap<String, Long> start_cooldowns = new HashMap<>();
    public HashMap<String, Long> vip_cooldowns = new HashMap<>();
    public HashMap<String, Long> svip_cooldowns = new HashMap<>();

    int start_cooldown = 43200;
    int vip_cooldown = 43200;
    int svip_cooldown = 43200;

    long minuty = 0;
    long sekundy = 0;

    private String oncooldown = ChatColor.RED + "Mozesz wziac ten kit za: ";

    public void kitstart(Player pl){

        ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
        ItemStack body = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE);
        ItemStack papu = new ItemStack(Material.COOKED_BEEF, 64);
        ItemStack jablka = new ItemStack(Material.GOLDEN_APPLE,4);

        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        body.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        leggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 3);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 2);

        ItemStack[] itemy = new ItemStack[] {helm,body,leggins,boots,sword,pickaxe,papu,jablka};

        Inventory inv = pl.getInventory();
        inv.addItem(itemy);
        pl.sendMessage(ChatColor.GREEN + "Przydzielono kit: " + ChatColor.RED + "Start");
        start_cooldowns.put(pl.getName(), System.currentTimeMillis());
    }

    public void kitvip(Player pl){

        ItemStack helm = new ItemStack(Material.IRON_HELMET);
        ItemStack body = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack leggins = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemStack papu = new ItemStack(Material.COOKED_BEEF,64);
        ItemStack jablka = new ItemStack(Material.GOLDEN_APPLE, 6);

        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,3);
        helm.addEnchantment(Enchantment.DURABILITY,2);
        body.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,3);
        body.addEnchantment(Enchantment.DURABILITY,2);
        leggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,3);
        leggins.addEnchantment(Enchantment.DURABILITY,2);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,3);
        boots.addEnchantment(Enchantment.DURABILITY,2);

        sword.addEnchantment(Enchantment.DAMAGE_ALL,4);
        sword.addEnchantment(Enchantment.KNOCKBACK,1);
        sword.addEnchantment(Enchantment.FIRE_ASPECT,1);

        pickaxe.addEnchantment(Enchantment.DIG_SPEED,4);
        pickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS,1);
        pickaxe.addEnchantment(Enchantment.DURABILITY,3);

        ItemStack[] itemy = new ItemStack[] {helm,body,leggins,boots,sword,pickaxe,papu,jablka};

        Inventory inv = pl.getInventory();
        inv.addItem(itemy);
        pl.sendMessage(ChatColor.GREEN + "Przydzielono kit: " + ChatColor.RED + "VIP");
        vip_cooldowns.put(pl.getName(), System.currentTimeMillis());
    }

    public void kitsvip(Player pl){

        ItemStack helm = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack body = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggins = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemStack papu = new ItemStack(Material.COOKED_BEEF,64);
        ItemStack jablka = new ItemStack(Material.GOLDEN_APPLE, 8);

        helm.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
        helm.addEnchantment(Enchantment.DURABILITY,3);
        body.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
        body.addEnchantment(Enchantment.DURABILITY,3);
        leggins.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
        leggins.addEnchantment(Enchantment.DURABILITY,3);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL,4);
        boots.addEnchantment(Enchantment.DURABILITY,3);

        sword.addEnchantment(Enchantment.DAMAGE_ALL,5);
        sword.addEnchantment(Enchantment.KNOCKBACK,1);
        sword.addEnchantment(Enchantment.FIRE_ASPECT,2);

        pickaxe.addEnchantment(Enchantment.DIG_SPEED,5);
        pickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS,2);
        pickaxe.addEnchantment(Enchantment.DURABILITY,3);

        ItemStack[] itemy = new ItemStack[] {helm,body,leggins,boots,sword,pickaxe,papu,jablka};

        Inventory inv = pl.getInventory();
        inv.addItem(itemy);
        pl.sendMessage(ChatColor.GREEN + "Przydzielono kit: " + ChatColor.RED + "SVIP");
        svip_cooldowns.put(pl.getName(), System.currentTimeMillis());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            Player pl = (Player) sender;
            if(args.length == 0) {
                pl.sendMessage(ChatColor.GREEN  + "Dostepne kity");
                for(int i = 0; i < kity.length; i++){
                    if(kity[i].equalsIgnoreCase("Start")){
                        if(start_cooldowns.containsKey(pl.getName())){
                            long secondLeft = ((start_cooldowns.get(pl.getName())/1000)+start_cooldown) - (System.currentTimeMillis()/1000);

                            if(secondLeft > 0){
                                pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.RED + "Start");
                            }
                            else{
                                pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.GREEN+ "Start");
                            }

                        }
                        else{
                            pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.GREEN + "Start");
                        }
                    }
                    else if(kity[i].equalsIgnoreCase("VIP")){

                        if (!sender.hasPermission("prison.vip")){
                            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTo tego KITu dostep maja gracze &eVIP i SVIP."));
                            return true;
                        }

                        if(vip_cooldowns.containsKey(pl.getName())){
                            long secondLeft = ((vip_cooldowns.get(pl.getName())/1000)+vip_cooldown) - (System.currentTimeMillis()/1000);
                            secondLeft = secondLeft / 60;
                            if(secondLeft > 0){
                                pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.RED + "VIP");
                            }
                            else{
                                pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.GREEN+ "VIP");
                            }

                        }
                        else{
                            pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.GREEN+ "VIP");
                        }
                    }
                    else if(kity[i].equalsIgnoreCase("SVIP")){

                        if (!sender.hasPermission("prison.svip")){
                            ChatUtil.sendMessage(sender, ChatUtil.fixColor("&cTo tego KITu dostep maja gracze &eSVIP."));
                            return true;
                        }

                        if(svip_cooldowns.containsKey(pl.getName())){
                            long secondLeft = ((svip_cooldowns.get(pl.getName())/1000)+svip_cooldown) - (System.currentTimeMillis()/1000);
                            secondLeft = secondLeft / 60;
                            if(secondLeft > 0){
                                pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.RED + "SVIP");
                            }
                            else{
                                pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.GREEN+ "SVIP");
                            }

                        }
                        else{
                            pl.sendMessage(ChatColor.GRAY + ">>" + ChatColor.GREEN + "SVIP");
                        }

                    }
                }

            }
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("start")){
                    if(start_cooldowns.containsKey(pl.getName())){
                        long secondLeft = ((start_cooldowns.get(pl.getName())/1000)+start_cooldown) - (System.currentTimeMillis()/1000);
                        if(secondLeft > 0){
                            secondLeft = TimeUnit.SECONDS.toMinutes(secondLeft);
                            pl.sendMessage(oncooldown + secondLeft + "minut");
                            return true;
                        }
                        else{
                            kitstart(pl);
                        }
                    }
                    else{
                        kitstart(pl);
                    }
                }
                else if(args[0].equalsIgnoreCase("vip")){

                    if (!pl.hasPermission("prison.vip")){
                        ChatUtil.sendMessage(pl, ChatUtil.fixColor("&cBrak uprawnien."));
                        return true;
                    }

                    if(vip_cooldowns.containsKey(pl.getName())){
                        long secondLeft = ((vip_cooldowns.get(pl.getName())/1000)+vip_cooldown) - (System.currentTimeMillis()/1000);
                        if(secondLeft > 0){
                            secondLeft = TimeUnit.SECONDS.toMinutes(secondLeft);
                            pl.sendMessage(oncooldown + secondLeft + "minut");
                            return true;
                        }
                        else{
                            kitvip(pl);
                        }
                    }
                    else{
                        kitvip(pl);
                    }
                }
                else if(args[0].equalsIgnoreCase("svip")){

                    if (!pl.hasPermission("prison.svip")){
                        ChatUtil.sendMessage(pl, ChatUtil.fixColor("&cBrak uprawnien."));
                        return true;
                    }

                    if(svip_cooldowns.containsKey(pl.getName())){
                        long secondLeft = ((svip_cooldowns.get(pl.getName())/1000)+svip_cooldown) - (System.currentTimeMillis()/1000);
                        if(secondLeft > 0){
                            secondLeft = TimeUnit.SECONDS.toMinutes(secondLeft);
                            pl.sendMessage(oncooldown + secondLeft + "minut");
                            return true;
                        }
                        else{
                            kitsvip(pl);
                        }
                    }
                    else{
                        kitsvip(pl);
                    }
                }
            }

            return true;
    }

}
