package pl.trollcraft.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class Board {

    private Player player;
    private Scoreboard scoreboard;

    public Board (Player player) {
        this.player = player;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

}
