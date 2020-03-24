package pl.trollcraft.chat;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ChatProfile {

    private static HashMap<Integer, ChatProfile> profiles = new HashMap<>();
    private static HashSet<Player> chatOff = new HashSet<>();

    private Player player;
    private boolean chatEnabled;
    private String lastMessage;
    private long lockedMessagingTill;

    public ChatProfile (Player player, boolean chatEnabled) {
        this.player = player;
        this.chatEnabled = chatEnabled;
        lastMessage = null;
        lockedMessagingTill = 0;
        profiles.put(player.getEntityId(), this);
    }

    // -------- -------- -------- -------- --------

    public boolean hasChatEnabled() { return chatEnabled; }
    public boolean canWrite() { return lockedMessagingTill == 0 || System.currentTimeMillis() > lockedMessagingTill; }

    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
        if (chatEnabled)
            chatOff.remove(player);
        else
            chatOff.add(player);
    }

    public boolean changeLastMessage(String lastMessage) {
        boolean same = false;

        if (lastMessage != null && lastMessage.equalsIgnoreCase(this.lastMessage)) same = true;
        else this.lastMessage = lastMessage;

        return same;
    }

    public void lockMessaging(long seconds) { lockedMessagingTill = System.currentTimeMillis() + 1000L * seconds; }

    public void save() {
        new BukkitRunnable() {

            @Override
            public void run() {
                YamlConfiguration conf = Configs.load("chatprofiles.yml", Main.getInstance());
                conf.set("chatprofiles." + player.getName() + ".chatEnabled", chatEnabled);
                Configs.save(conf, "chatprofiles.yml");
            }

        }.runTaskAsynchronously(Main.getInstance());
    }

    // -------- -------- -------- -------- --------

    public static HashSet<Player> getChatOff() { return chatOff; }

    public static ChatProfile get(Player player) {
        return profiles.get(player.getEntityId());
    }

    // -------- -------- -------- -------- --------

    public static void load(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                boolean chatEnabled = true;

                if (player.hasPermission("prison.vip")){
                    String name = player.getName();
                    YamlConfiguration conf = Configs.load("chatprofiles.yml", Main.getInstance());
                    if (conf.contains("chatprofiles." + name))
                        chatEnabled = conf.getBoolean("chatprofiles." + name + ".chatEnabled");
                }

                new ChatProfile(player, chatEnabled);
            }

        }.runTaskAsynchronously(Main.getInstance());
    }

}
