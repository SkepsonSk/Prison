package pl.trollcraft.lootchests;

import pl.trollcraft.lootchests.rewards.Reward;

import java.util.Random;

public class LootItem implements Comparable<LootItem> {

    public enum Rarity {

        COMMON("&7Pospolita", 90, 100),
        UNCOMMON("&bNiepospolita", 70, 90),
        RARE("&eRzadka", 50, 70),
        UNIQUE("&cUnikalna", 30, 50),
        LEGENDARY("&6Legendarna", 10, 30);

        private final Random RAND = new Random();

        private String name;
        private double minChance;
        private double maxChance;

        Rarity(String name, double minChance, double maxChance){
            this.name = name.replaceAll("&", "§");
            this.minChance = minChance;
            this.maxChance = maxChance;
        }

        public String getName() { return name; }

        public double getMinChance() { return minChance; }
        public double getMaxChance() { return maxChance; }

        public double getChance() {
            return minChance + (maxChance - minChance) * RAND.nextDouble();
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
