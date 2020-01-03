package pl.trollcraft.util.enchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class BlastEnchantment extends Enchantment {

    public BlastEnchantment() {
        super(19060);
    }

    @Override
    public String getName() {
        return "Blast";
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        Material t = itemStack.getType();
        return (t == Material.DIAMOND_PICKAXE ||
                t == Material.GOLD_PICKAXE ||
                t == Material.IRON_PICKAXE ||
                t == Material.STONE_PICKAXE ||
                t == Material.WOOD_PICKAXE);
    }

    public static Block[] blast(int lvl, Block c) {
        Block blocks[];
        if (lvl == 0) {
            blocks = new Block[8+1];
            Bukkit.getConsoleSender().sendMessage("ddd");
            blocks[0] = c.getRelative(BlockFace.NORTH);
            blocks[1] = c.getRelative(BlockFace.SOUTH);
            blocks[2] = c.getRelative(BlockFace.EAST);
            blocks[3] = c.getRelative(BlockFace.WEST);
            blocks[4] = c.getRelative(BlockFace.NORTH_EAST);
            blocks[5] = c.getRelative(BlockFace.NORTH_WEST);
            blocks[6] = c.getRelative(BlockFace.SOUTH_EAST);
            blocks[7] = c.getRelative(BlockFace.SOUTH_WEST);
            blocks[8] = c;
            return blocks;
        }
        else if (lvl == 1) {
            blocks = new Block[8*2+2];
            Block down = c.getRelative(BlockFace.DOWN);
            blocks[0] = c.getRelative(BlockFace.NORTH);
            blocks[1] = c.getRelative(BlockFace.SOUTH);
            blocks[2] = c.getRelative(BlockFace.EAST);
            blocks[3] = c.getRelative(BlockFace.WEST);
            blocks[4] = c.getRelative(BlockFace.NORTH_EAST);
            blocks[5] = c.getRelative(BlockFace.NORTH_WEST);
            blocks[6] = c.getRelative(BlockFace.SOUTH_EAST);
            blocks[7] = c.getRelative(BlockFace.SOUTH_WEST);
            blocks[8] = down.getRelative(BlockFace.NORTH);
            blocks[9] = down.getRelative(BlockFace.SOUTH);
            blocks[10] = down.getRelative(BlockFace.EAST);
            blocks[11] = down.getRelative(BlockFace.WEST);
            blocks[12] = down.getRelative(BlockFace.NORTH_EAST);
            blocks[13] = down.getRelative(BlockFace.NORTH_WEST);
            blocks[14] = down.getRelative(BlockFace.SOUTH_EAST);
            blocks[15] = down.getRelative(BlockFace.SOUTH_WEST);
            blocks[16] = c;
            blocks[17] = down;
            return blocks;
        }
        else if (lvl == 2) {
            blocks = new Block[8*3+3];
            Block top = c.getRelative(BlockFace.UP);
            Block down = c.getRelative(BlockFace.DOWN);
            blocks[0] = c.getRelative(BlockFace.NORTH);
            blocks[1] = c.getRelative(BlockFace.SOUTH);
            blocks[2] = c.getRelative(BlockFace.EAST);
            blocks[3] = c.getRelative(BlockFace.WEST);
            blocks[4] = c.getRelative(BlockFace.NORTH_EAST);
            blocks[5] = c.getRelative(BlockFace.NORTH_WEST);
            blocks[6] = c.getRelative(BlockFace.SOUTH_EAST);
            blocks[7] = c.getRelative(BlockFace.SOUTH_WEST);
            blocks[8] = down.getRelative(BlockFace.NORTH);
            blocks[9] = down.getRelative(BlockFace.SOUTH);
            blocks[10] = down.getRelative(BlockFace.EAST);
            blocks[11] = down.getRelative(BlockFace.WEST);
            blocks[12] = down.getRelative(BlockFace.NORTH_EAST);
            blocks[13] = down.getRelative(BlockFace.NORTH_WEST);
            blocks[14] = down.getRelative(BlockFace.SOUTH_EAST);
            blocks[15] = down.getRelative(BlockFace.SOUTH_WEST);
            blocks[16] = top.getRelative(BlockFace.NORTH);
            blocks[17] = top.getRelative(BlockFace.SOUTH);
            blocks[18] = top.getRelative(BlockFace.EAST);
            blocks[19] = top.getRelative(BlockFace.WEST);
            blocks[20] = top.getRelative(BlockFace.NORTH_EAST);
            blocks[21] = top.getRelative(BlockFace.NORTH_WEST);
            blocks[22] = top.getRelative(BlockFace.SOUTH_EAST);
            blocks[23] = top.getRelative(BlockFace.SOUTH_WEST);
            blocks[24] = c;
            blocks[25] = top;
            blocks[26] = down;
            return blocks;
        }
        return null;
    }

}
