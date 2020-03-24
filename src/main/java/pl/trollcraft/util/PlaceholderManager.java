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

        else if (iden.contains("mtoprom")) {
            double p = Utils.getPercentageToPromotionMoney(player);
            if (p >= 100) return "&a&lOK";
            else return "&f&l" + p + "%";
        }

        else if (iden.contains("btoprom")) {
            double p = Utils.getPercentageToPromotionBlocks(player);
            if (p >= 100) return "&a&lOK";
            else return "&f&l" + p + "%";
        }

        return "n/a";
    }
}
