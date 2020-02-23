package pl.trollcraft.lootchests;

public class KeyDropChance implements Comparable<KeyDropChance> {

    private int id;
    private double chance;

    public KeyDropChance (LootChest lootChest) {
        id = lootChest.getId();
        chance = lootChest.getKeyDropChance();
    }

    public int getId() { return id; }
    public double getChance() { return chance; }

    @Override
    public int compareTo(KeyDropChance keyDropChance) {
        if (chance > keyDropChance.chance) return 1;
        else if (chance < keyDropChance.chance) return -1;
        else return 0;
    }
}
