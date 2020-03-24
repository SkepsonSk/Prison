package pl.trollcraft.loginrewards;

import org.bukkit.inventory.ItemStack;
import pl.trollcraft.lootchests.rewards.Reward;

import java.util.ArrayList;

public class LoginReward {

    private static ArrayList<LoginReward> loginRewards = new ArrayList<>();

    private int day;
    private ItemStack guiIcon;
    private Reward reward;

    public LoginReward (int day, ItemStack guiIcon, Reward reward) {
        this.day = day;
        this.guiIcon = guiIcon;
        this.reward = reward;
        loginRewards.add(this);
    }

    public ItemStack getGuiIcon() { return guiIcon; }
    public Reward getReward() { return reward; }

    public static LoginReward get(int day){
        for (LoginReward lr : loginRewards)
            if (lr.day == day) return lr;
        return null;
    }

}
