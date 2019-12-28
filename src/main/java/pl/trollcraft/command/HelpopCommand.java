package pl.trollcraft.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HelpopCommand implements CommandExecutor {

    private static final HashMap<UUID, Long> times = new HashMap();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefix") + "&8>> &cPoprawne uzycie: /"));
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        Long t = times.get(((Player) sender).getUniqueId());
        if (t != null && System.currentTimeMillis() - t.longValue() < TimeUnit.SECONDS.toMillis(30)) {
            return ChatUtil.sendMessage(sender, "&8>> &7Na helpop mozesz pisac dopiero za 30 s!");
        }
        String msg = sb.toString().trim();
        sender.sendMessage(ChatUtil.fixColor("&8>> &7Wyslales wiadomosc o tresci: &6" + msg));
        for (Player p1 : Bukkit.getOnlinePlayers()) {
            if (p1.hasPermission("tools.helpop.admin")) {
                times.put(((Player) sender).getUniqueId(), Long.valueOf(System.currentTimeMillis()));
                ChatUtil.sendMessageToAllAdmins(ChatUtil.fixColor("&7[&4&lHELPOP&7] nick&8: &btresc").replace("nick", sender.getName()).replace("tresc", msg), "prison.helpop");
                return true;
            }
        }
        return false;
    }
}
