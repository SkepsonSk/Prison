package pl.trollcraft.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.trollcraft.mineractions.MineExecutor;
import pl.trollcraft.obj.PrisonBlock;
import pl.trollcraft.obj.booster.PlayerBooster;
import pl.trollcraft.obj.cells.Cell;
import pl.trollcraft.obj.Warp;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.MinersManager;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        AutoSell.load(player);
        Cell.load(player);
        PrisonBlock.assignToPlayer(player);
        MinersManager.load(player);

        boolean hasBooster = PlayerBooster.load(player);
        if (hasBooster){
            PlayerBooster booster = PlayerBooster.getBooster(player);
            ChatUtil.sendMessage(player, ChatUtil.fixColor("&aPosiadasz aktywny &eBOOSTER!\n&7(Bonus: &ex" + booster.getBonus() + ", &7aktywny jeszcze przez &e" + booster.getSeconds() + " sekund)"));
        }
        PlayerBooster.sendGlobalBoosterInfo(player);

        Warp.get("spawn").teleport(player);
        sendWelcomeMessage(player);
    }

    private void sendWelcomeMessage(Player player) {

        for (int i = 0 ; i < 50 ; i++)
            player.sendMessage("");

        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Witaj na &7&lPRISONie!"));
        player.sendMessage("");
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Wersja serwera, na ktorej wlasnie grasz"));
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7jest wersja &8&lBETA."));
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Oznacza to, ze w pewnych aspektach wciaz"));
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7moga pojawiac sie bledy i niedociagniecia."));
        player.sendMessage("");
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Po zakonczeniu BETY (TESTOW) wszystkie"));
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7statystyki zostana zresetowane."));
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7(tj. wykopane bloki, pieniadze, eq, itp...)"));
        player.sendMessage("");
        ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Za udzial w BECIE przewidziane sa nagrody."));
        player.sendMessage("");
    }

}
