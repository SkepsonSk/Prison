package pl.trollcraft.mineractions;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class MinerAction {

    private static ArrayList<MinerAction> actions = new ArrayList<>();
    private static HashMap<String, Object> dataLayer = new HashMap<>();

    public MinerAction() { actions.add(this); }

    // -------- -------- -------- -------- --------

    public abstract boolean canBePerformed(Player player);
    public abstract void prepare(Player player);
    public abstract void perform(Player player, Material material, byte data);

    // -------- -------- -------- -------- --------

    public static ArrayList<MinerAction> getActions() { return actions; }

    // -------- -------- -------- -------- --------

    public static void registerData(String tag, Object data) {
        if (dataLayer.containsKey(tag)) dataLayer.replace(tag, data);
        else dataLayer.put(tag, data);
    }

    public static Object getData(String tag) {
        if (dataLayer.containsKey(tag)) return dataLayer.get(tag);
        return null;
    }

}
