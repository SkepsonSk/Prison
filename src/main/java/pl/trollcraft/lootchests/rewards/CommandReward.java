package pl.trollcraft.lootchests.rewards;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandReward implements Reward {

    private String name;
    private String command;

    public CommandReward (String name, String command) {
        this.name = name;
        this.command = command;
    }

    public String getName() { return name; }
    public String getCommand() { return command; }

    @Override
    public void give(Player player) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                .replaceAll("_", " ")
                .replaceAll("%player%", player.getName()));
    }

    @Override
    public ItemStack getItem() {
        Bukkit.getConsoleSender().sendMessage("nazwa: " + name);
        ItemStack i = new ItemStack(Material.ENDER_PEARL);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(name);
        i.setItemMeta(itemMeta);
        return i;
    }
}
