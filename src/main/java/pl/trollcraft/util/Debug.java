package pl.trollcraft.util;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Debug {

    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void log(String a) {
        console.sendMessage(a.replace("&", "§"));
    }

}
