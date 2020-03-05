package pl.trollcraft.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import pl.trollcraft.obj.PrisonBlock;
import tesdev.Money.MoneyAPI;
import tesdev.Money.TockensAPI;

public class PlaceholderManager extends PlaceholderExpansion {

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "prison";
    }

    @Override
    public String getAuthor() {
        return "SkepsonSk";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String iden) {

        if (iden.contains("mined"))
            return String.valueOf(MinersManager.get(player));
        else if (iden.contains("block"))
            return PrisonBlock.getPlayerBlock(player).getName();
        else if (iden.contains("money"))
            return String.valueOf(Utils.round(MoneyAPI.getInstance().getMoney(player), 3));
        else if (iden.contains("tokens"))
            return String.valueOf(TockensAPI.getInstance().getTockens(player));

        return "n/a";

    }
}
