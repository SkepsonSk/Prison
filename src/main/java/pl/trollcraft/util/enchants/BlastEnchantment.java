package pl.trollcraft.util.enchants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
        return 3;
    }

    @Override
    public int getStartLevel() {
        return 1;
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

    public static ArrayList<Block> blast(int lvl, Block c) {

        ArrayList<Block> blocks = new ArrayList<>(32);

        if (lvl == 1) {
            blocks.add(c.getRelative(BlockFace.NORTH));
            blocks.add(c.getRelative(BlockFace.SOUTH));
            blocks.add(c.getRelative(BlockFace.EAST));
            blocks.add(c.getRelative(BlockFace.WEST));
            blocks.add(c.getRelative(BlockFace.NORTH_EAST));
            blocks.add(c.getRelative(BlockFace.NORTH_WEST));
            blocks.add(c.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(c.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(c);
            return blocks;
        }
        else if (lvl == 2) {
            Block down = c.getRelative(BlockFace.DOWN);
            blocks.add(c.getRelative(BlockFace.NORTH));
            blocks.add(c.getRelative(BlockFace.SOUTH));
            blocks.add(c.getRelative(BlockFace.EAST));
            blocks.add(c.getRelative(BlockFace.WEST));
            blocks.add(c.getRelative(BlockFace.NORTH_EAST));
            blocks.add(c.getRelative(BlockFace.NORTH_WEST));
            blocks.add(c.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(c.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(down.getRelative(BlockFace.NORTH));
            blocks.add(down.getRelative(BlockFace.SOUTH));
            blocks.add(down.getRelative(BlockFace.EAST));
            blocks.add(down.getRelative(BlockFace.WEST));
            blocks.add(down.getRelative(BlockFace.NORTH_EAST));
            blocks.add(down.getRelative(BlockFace.NORTH_WEST));
            blocks.add(down.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(down.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(c);
            blocks.add(down);
            return blocks;
        }
        else if (lvl == 3) {
            Block top = c.getRelative(BlockFace.UP);
            Block down = c.getRelative(BlockFace.DOWN);
            blocks.add(c.getRelative(BlockFace.NORTH));
            blocks.add(c.getRelative(BlockFace.SOUTH));
            blocks.add(c.getRelative(BlockFace.EAST));
            blocks.add(c.getRelative(BlockFace.WEST));
            blocks.add(c.getRelative(BlockFace.NORTH_EAST));
            blocks.add(c.getRelative(BlockFace.NORTH_WEST));
            blocks.add(c.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(c.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(down.getRelative(BlockFace.NORTH));
            blocks.add(down.getRelative(BlockFace.SOUTH));
            blocks.add(down.getRelative(BlockFace.EAST));
            blocks.add(down.getRelative(BlockFace.WEST));
            blocks.add(down.getRelative(BlockFace.NORTH_EAST));
            blocks.add(down.getRelative(BlockFace.NORTH_WEST));
            blocks.add(down.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(down.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(top.getRelative(BlockFace.NORTH));
            blocks.add(top.getRelative(BlockFace.SOUTH));
            blocks.add(top.getRelative(BlockFace.EAST));
            blocks.add(top.getRelative(BlockFace.WEST));
            blocks.add(top.getRelative(BlockFace.NORTH_EAST));
            blocks.add(top.getRelative(BlockFace.NORTH_WEST));
            blocks.add(top.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(top.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(c);
            blocks.add(top);
            blocks.add(down);
            return blocks;
        }
        return null;
    }

}
