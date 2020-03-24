package pl.trollcraft.lootchests;

import pl.trollcraft.lootchests.rewards.Reward;

import java.util.Random;

public class LootItem implements Comparable<LootItem> {

    public enum Rarity {

        COMMON("&7Pospolita", 100),
        UNCOMMON("&bNiepospolita", 70),
        RARE("&eRzadka", 50),
        UNIQUE("&cUnikalna", 30),
        LEGENDARY("&6Legendarna", 10);

        private final Random RAND = new Random();

        private String name;
        private double chance;

        Rarity(String name, double chance){
            this.name = name.replaceAll("&", "§");
            this.chance = chance;
        }

        public String getName() { return name; }

        public double getChance() {
            return chance;
        }

    }

    private Reward reward;
    private Rarity rarity;
    private double chance;

    public LootItem (Reward reward, Rarity rarity) {
        this.reward = reward;
        this.rarity = rarity;
        chance = rarity.getChance();
    }

    public Reward getReward() { return reward; }
    public Rarity getRarity() { return rarity; }
    public double getChance() { return chance; }

    // -------- -------- -------- -------- -------- -------

    @Override
    public int compareTo(LootItem lootItem) {
        if (chance > lootItem.chance) return 1;
        else if (chance < lootItem.chance) return -1;
        else return 0;
    }

}
