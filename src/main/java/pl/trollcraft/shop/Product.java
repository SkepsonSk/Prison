package pl.trollcraft.shop;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tesdev.Money.MoneyAPI;
import tesdev.Money.api.EconomyProfile;

public class Product {

    private ItemStack icon;
    private ItemStack itemStack;
    private double price;

    public Product(ItemStack icon, ItemStack itemStack, double price) {
        this.icon = icon;
        this.itemStack = itemStack;
        this.price = price;
    }

    public boolean canBuy(Player player) {
        double money = EconomyProfile.FastAccess.getMoney(player);
        return money >= price;
    }

    public void buy(Player player) {
        EconomyProfile.FastAccess.takeMoney(player, price);
        //MoneyAPI.getInstance().removeMoney(player, price);
        player.getInventory().addItem(itemStack);
    }

    public ItemStack getIcon() { return icon; }
    public ItemStack getItemStack() { return itemStack; }
    public double getPrice() { return price; }

}
