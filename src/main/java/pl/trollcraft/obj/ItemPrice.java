package pl.trollcraft.obj;

import org.bukkit.Material;

public class ItemPrice {

    private Material material;
    private byte data;
    private double price;

    public ItemPrice (Material material, byte data, double price) {
        this.material = material;
        this.data = data;
        this.price = price;
    }

    public Material getMaterial() { return material; }
    public byte getData() { return data; }
    public double getPrice() { return price; }

}
