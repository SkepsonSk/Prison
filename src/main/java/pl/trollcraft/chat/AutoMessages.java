package pl.trollcraft.chat;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.Configs;

import java.util.ArrayList;
import java.util.HashMap;

public class AutoMessages {

    private static HashMap<Player, Boolean> autoMessagesEnabled = new HashMap<>();
    private static ArrayList<String> messages = new ArrayList<>();
    private static int index = 0;

    public static void init() {

        YamlConfiguration conf = Configs.load("automessages.yml", Main.getInstance());
        long interval = conf.getLong("automessages.interval") * 20l;
        conf.getStringList("automessages.messages").forEach( msg -> messages.add(ChatUtil.fixColor(msg)) );

        new BukkitRunnable() {

            @Override
            public void run() {

                String message = messages.get(index);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    ChatUtil.sendMessage(p, "");
                    ChatUtil.sendMessage(p, message);
                    ChatUtil.sendMessage(p, "");
                }

                if (index == messages.size() - 1)
                    index = 0;
                else
                    index++;

            }

        }.runTaskTimerAsynchronously(Main.getInstance(), interval, interval);


    }

}
