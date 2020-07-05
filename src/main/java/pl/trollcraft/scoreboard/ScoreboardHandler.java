package pl.trollcraft.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardHandler {

    public static void setScoreboard(Player player) {

        Warrior warrior = WarriorsManager.get(player);

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective health = board.registerNewObjective("health", "health");
        health.setDisplayName(ChatColor.RED + "❤");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);

        Objective obj = board.registerNewObjective("§4§lPVP", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score empty1 = obj.getScore(ChatColor.WHITE + "");
        empty1.setScore(15);

        Score killsName = obj.getScore("§7§lZabic");
        killsName.setScore(14);

        Team kills = board.registerNewTeam("kills");
        kills.addEntry(ChatColor.BLACK + "" + ChatColor.WHITE);
        kills.setPrefix("   §c§l" + warrior.getKills());
        kills.setDisplayName(ChatColor.DARK_GREEN + "        " + ChatColor.RED);
        obj.getScore(ChatColor.BLACK + "" + ChatColor.WHITE).setScore(13);

        Score deathsName = obj.getScore("§7§lSmierci");
        deathsName.setScore(13);

        Team deaths = board.registerNewTeam("deaths");
        deaths.addEntry(ChatColor.BLACK + "" + ChatColor.RED);
        deaths.setPrefix("   §c§l" + warrior.getDeaths());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.RED).setScore(12);

        Score empty2 = obj.getScore(ChatColor.BLACK + "");
        empty2.setScore(11);

        Score kdrName = obj.getScore("§7§lKDR");
        kdrName.setScore(10);

        Team kdr = board.registerNewTeam("kdr");
        kdr.addEntry(ChatColor.GREEN + "" + ChatColor.RED);
        kdr.setPrefix("   §c§l" + warrior.getKdrFormatted());
        obj.getScore(ChatColor.GREEN + "" + ChatColor.RED).setScore(9);

        Score empty3 = obj.getScore(ChatColor.RED + "");
        empty3.setScore(8);

        Score killStreakName = obj.getScore("§7§lSeria");
        killStreakName.setScore(7);

        Team killStreak = board.registerNewTeam("killStreak");
        killStreak.addEntry(ChatColor.YELLOW + "" + ChatColor.RED);
        killStreak.setPrefix("   §c§l" + warrior.getKillStreak());
        obj.getScore(ChatColor.YELLOW + "" + ChatColor.RED).setScore(6);

        Score empty4 = obj.getScore(ChatColor.YELLOW + "");
        empty4.setScore(5);

        Score toPromotionName = obj.getScore("§7§lDo awansu");
        toPromotionName.setScore(4);

        Team toPromotion = board.registerNewTeam("toPromotion");
        toPromotion.addEntry(ChatColor.DARK_PURPLE + "" + ChatColor.RED);
        toPromotion.setPrefix("   §c§l" + warrior.getKillsToPromotion());
        obj.getScore(ChatColor.DARK_PURPLE + "" + ChatColor.RED).setScore(3);

        player.setScoreboard(board);
    }

    public static void update(Player player) {

        Warrior warrior = WarriorsManager.get(player);

        Scoreboard board = player.getScoreboard();

        board.getTeam("kills").setPrefix("   §c§l" + warrior.getKills());
        board.getTeam("deaths").setPrefix("   §c§l" + warrior.getDeaths());
        board.getTeam("kdr").setPrefix("   §c§l" + warrior.getKdrFormatted());
        board.getTeam("killStreak").setPrefix("   §c§l" + warrior.getKillStreak());
        board.getTeam("toPromotion").setPrefix("   §c§l" + warrior.getKillsToPromotion());

    }

}
