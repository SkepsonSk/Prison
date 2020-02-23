package pl.trollcraft.obj;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class Item {

    private Material material;
    private byte data;
    private HashMap<Integer, FortuneData> fortuneData;

    public Item(Material material, byte data) {
        this.material = material;
        this.data = data;
        fortuneData = null;
    }

    public Item(Material material, byte data, HashMap<Integer, FortuneData> fortuneData) {
        this.material = material;
        this.data = data;
        this.fortuneData = fortuneData;
    }

    public Material getMaterial() { return material; }
    public byte getData() { return data; }
    public HashMap<Integer, FortuneData> getFortuneData() { return fortuneData; }

    public ItemStack toItemStack() {
        return new ItemStack(material, 1, data);
    }

}
