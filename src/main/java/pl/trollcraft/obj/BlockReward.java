package pl.trollcraft.obj;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.MinersManager;
import pl.trollcraft.util.Utils;
import tesdev.Money.MoneyAPI;
import tesdev.Money.TockensAPI;

import java.util.ArrayList;

public class BlockReward {

    private static ArrayList<BlockReward> rewards = new ArrayList<>();

    private int every;
    private String title;
    private String subtitle;
    private double money;
    private int tokens;
    private ItemStack itemStack;

    public BlockReward(int every, String title, String subtitle, double money, int tokens, ItemStack itemStack){
        this.every = every;
        this.title = title;
        this.subtitle = subtitle;
        this.money = money;
        this.tokens = tokens;
        this.itemStack = itemStack;
        rewards.add(this);
    }

    public void reward(Player player) {

        if (money != 0) {
            MoneyAPI.getInstance().addMoney(player, money);
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&a&l+" + money));
        }
        if (tokens != 0) {
            TockensAPI.getInstance().addTockens(player, money);
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&6&l+" + tokens));
        }
        if (itemStack != null)
            player.getInventory().addItem(itemStack);
    }

    public static void load() {

        YamlConfiguration conf = Configs.load("blockrewards.yml", Main.getInstance());

        conf.getConfigurationSection("rewards").getKeys(false).forEach( every -> {
            String title = conf.getString("rewards." + every + ".title");
            String subtitle = conf.getString("rewards." + every + ".subtitle");

            double money = 0;
            if (conf.contains("rewards." + every + ".money"))
                money = conf.getDouble("rewards." + every + ".money");

            int tokens = 0;
            if (conf.contains("rewards." + every + ".tokens"))
                tokens = conf.getInt("rewards." + every + ".tokens");

            ItemStack itemStack = null;
            if (conf.contains("rewards." + every + ".item"))
                itemStack = Utils.deserialize(conf, "rewards." + every + ".item");

            new BlockReward(Integer.parseInt(every), title, subtitle, money, tokens, itemStack);
        } );

    }

    public static void rewardPlayer(Player player) {
        int b = MinersManager.get(player);
        boolean titled = false;
        for (BlockReward blockReward : rewards) {
            if (b % blockReward.every == 0) {
                blockReward.reward(player);

                if (!titled){
                    titled = true;
                    ChatUtil.sendMessage(player, ChatUtil.fixColor(blockReward.title));
                }
                else
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&aOtrzymujesz takze nagrode za wykopanie &e" + blockReward.every + " blokow."));

            }
        }
    }

}
