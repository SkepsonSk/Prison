package pl.trollcraft.mineractions.actions;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pl.trollcraft.mineractions.MinerAction;
import pl.trollcraft.util.MinersManager;

public class MinedBlocksAction extends MinerAction {

    @Override
    public boolean canBePerformed(Player player) { return true; }

    @Override
    public void prepare(Player player) { return; }

    @Override
    public void perform(Player player, Block block) { MinersManager.add(player, 1); }

}
