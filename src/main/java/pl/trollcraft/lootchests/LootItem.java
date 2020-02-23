package pl.trollcraft.lootchests;

import org.bukkit.inventory.ItemStack;

public class LootItem implements Comparable<LootItem> {

    public enum Rarity {

        COMMON("&7Pospolita", 100),
        UNCOMMON("&bNiepospolita", 75),
        RARE("&eRzadka", 50),
        UNIQUE("&cUnikalna", 30),
        LEGENDARY("&6Legendarna", 10);

        private String name;
        private float chance;

        Rarity(String name, float chance){
            this.name = name.replaceAll("&", "§");
            this.chance = chance;
        }

        public float getChance() { return chance; }

    }

    private ItemStack itemStack;
    private Rarity rarity;

    public LootItem (ItemStack itemStack, Rarity rarity) {
        this.itemStack = itemStack;
        this.rarity = rarity;
    }

    public ItemStack getItemStack() { return itemStack; }
    public Rarity getRarity() { return rarity; }

    // -------- -------- -------- -------- -------- -------

    @Override
    public int compareTo(LootItem lootItem) {
        float thisChance = rarity.chance;
        float secoChance = lootItem.rarity.chance;
        if (thisChance > secoChance) return 1;
        else if (thisChance < secoChance) return -1;
        else return 0;
    }

}
