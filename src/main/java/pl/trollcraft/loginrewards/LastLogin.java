package pl.trollcraft.loginrewards;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LastLogin {

    private static ArrayList<LastLogin> lastLogins = new ArrayList<>();

    private Player player;
    private int rewardDay;
    private Date lastLogin;

    public LastLogin(Player player, int rewardDay, Date lastLogin) {
        this.player = player;
        this.rewardDay = rewardDay;
        this.lastLogin = lastLogin;
        lastLogins.add(this);
    }

    public boolean loggedYesterday() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        Calendar today = Calendar.getInstance();
        today.setTime(lastLogin);
        return (yesterday.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) &&
                yesterday.get(Calendar.YEAR) == today.get(Calendar.YEAR));
    }

    public boolean loggedToday() {
        Calendar today = Calendar.getInstance();
        Calendar checked = Calendar.getInstance();
        checked.setTime(lastLogin);
        return (today.get(Calendar.DAY_OF_YEAR) == checked.get(Calendar.DAY_OF_YEAR) &&
                today.get(Calendar.YEAR) == checked.get(Calendar.YEAR));
    }

    public void loginToday() {
        lastLogin.setTime(System.currentTimeMillis());
    }

    public LoginReward getReward() {
        if (loggedYesterday()){
            loginToday();
            return LoginReward.get(rewardDay);
        }
        else if (!loggedToday())
            rewardDay = 1;
        return null;
    }

}
