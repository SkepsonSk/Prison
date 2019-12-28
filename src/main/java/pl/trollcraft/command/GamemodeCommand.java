package pl.trollcraft.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;

public class GamemodeCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String lab, String[] args) {
        Player p = (Player)sender;
        if (!p.hasPermission("prison.gm")) {
            p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &cNie dla psa kielbasa!"));
            return false;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {

                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&7Twoj tryb gry zostal zmieniony na &a&lSURVIVAL"));
                return true;
            }
            if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {

                p.setGameMode(GameMode.CREATIVE);
                p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&7Twoj tryb gry zostal zmieniony na &a&lCREATIVE"));
                return true;
            }
            if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {

                p.setGameMode(GameMode.ADVENTURE);
                p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Twoj tryb gry zostal zmieniony na &a&lADVENTURE"));
                return true;
            }
            if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {

                p.setGameMode(GameMode.SPECTATOR);
                p.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Twoj tryb gry zostal zmieniony na &a&lSPECTOR"));
                return true;
            }
        }
        if (args.length != 2) {
            sender.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Poprawne uzycie /gm <1/2/3> (nick)"));
            return false;
        }
        Player cel = Bukkit.getPlayerExact(args[1]);
        if (cel == null) {
            sender.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Podany gracz jest &4&lOFFLINE"));
            Player p2 = (Player)sender;
            return false;
        }
        if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s")) {
            cel.setGameMode(GameMode.SURVIVAL);
            cel.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Twoj tryb gry zostal zmieniony na &a&lSURVIVAL &7przez " + sender.getName()));
            sender.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &cZmieniles tryb gry gracza " + cel.getName() + " na &a&lSURVIVAL"));
            return true;
        }
        if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
            cel.setGameMode(GameMode.CREATIVE);
            cel.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Twoj tryb gry zostal zmieniony na &a&lCREATIVE &7przez " + sender.getName()));
            sender.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Zmieniles tryb gry gracza " + cel.getName() + " na &a&lCREATIVE"));
            return true;
        }
        if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
            cel.setGameMode(GameMode.ADVENTURE);
            cel.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Twoj tryb gry zostal zmieniony na &a&lADVENTURE &7przez " + sender.getName()));
            sender.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Zmieniles tryb gry gracza " + cel.getName() + " na &a&lADENTURE"));
            return true;
        }
        if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
            cel.setGameMode(GameMode.SPECTATOR);
            cel.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Twoj tryb gry zostal zmieniony na &a&lSPECTOR &7przez " + sender.getName()));
            sender.sendMessage(ChatUtil.fixColor(Main.getInstance().getConfig().get("prefixColor") + "&8>> &7Zmieniles tryb gry gracza " + cel.getName() + " na &a&lSPECTOR"));
            return true;
        }
        return false;
    }

}
