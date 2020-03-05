package pl.trollcraft.mineractions;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;
import pl.trollcraft.lootchests.LootChest;
import pl.trollcraft.obj.BlockReward;
import pl.trollcraft.selling.SellingUtils;
import pl.trollcraft.util.AutoSell;
import pl.trollcraft.util.ChatUtil;
import pl.trollcraft.util.DropManager;
import pl.trollcraft.util.MinersManager;
import pl.trollcraft.util.enchants.BlastEnchantment;
import tesdev.Money.MoneyAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MinerListener implements Listener {

    private static final String WORLD_ALLOWED = "world";

    /*@EventHandler
    public void onMine (BlockBreakEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);

        Player player = event.getPlayer();
        Block[] blocksAll = getBlocksMined(player, event.getBlock());
        ArrayList<Block> blocks = new ArrayList<>();

        for (int i = 0 ; i < blocksAll.length ; i++)
            if (canBreakBlocks(player, blocksAll[i])) blocks.add(blocksAll[i]);

        ArrayList<MinerAction> actions = new ArrayList<>();
        for (MinerAction action : MinerAction.getActions()) {
            if (action.canBePerformed(player)) {
                actions.add(action);
                action.prepare(player);
            }
        }

        for (Block b : blocks)
            for (MinerAction action : actions) action.perform(player, b);

    }*/

    @EventHandler (priority = EventPriority.LOWEST)
    public void onMine (BlockBreakEvent event) {
        if (event.isCancelled()) return;
        event.setCancelled(true);

        Player player = event.getPlayer();

        List<Block> blocks = getBlocksMined(event.getPlayer(), event.getBlock());
        List<Block> blocksCopy = new ArrayList<>(32);

        ArrayList<Material> materials = new ArrayList<>(32);
        ArrayList<Byte> data = new ArrayList<>(32);

        Block b;
        int ind = 0;
        for (int i = 0 ; i < blocks.size() ; i++) {

            b = blocks.get(i);
            if (canBreakBlocks(player, b)){
                blocksCopy.add(b);
                materials.add(b.getType());
                data.add(b.getData());
                ind++;
            }

        }

        MineExecutor.mine(player, materials, data);

        for (Block bl : blocksCopy)
            breakBlock(player, bl);
    }

    private List<Block> getBlocksMined(Player player, Block block) {
        ItemStack itemStack = player.getItemInHand();
        if (!player.getWorld().getName().equals(WORLD_ALLOWED)) return Arrays.asList(new Block[] { block });

        int lvl = hasBlast(itemStack);

        if (lvl != -1)
            return BlastEnchantment.blast(lvl, block);
        return Arrays.asList(new Block[] { block });
    }

    private void breakBlock(Player p, Block block) {
        if (canBreakBlocks(p, block))
            block.setType(Material.AIR);
    }

    private static WorldGuardPlugin worldGuardPlugin = Main.getWorldGuardPlugin();

    private static boolean canBreakBlocks(Player player, Block block) {
        RegionContainer container = worldGuardPlugin.getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(block.getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        return set.testState(localPlayer, DefaultFlag.BLOCK_BREAK);
    }

    private int hasBlast(ItemStack i) {
        for (Map.Entry<Enchantment, Integer> en : i.getEnchantments().entrySet()){
            if (en.getKey().getId() == 19060) return en.getValue();
        }
        return -1;
    }

}
