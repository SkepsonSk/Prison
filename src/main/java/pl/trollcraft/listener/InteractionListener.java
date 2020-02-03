package pl.trollcraft.listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.envoy.EnvoyChest;
import pl.trollcraft.util.ChatUtil;

public class InteractionListener implements Listener {

    private final static String KEY_ITEM_DISPLAYNAME = "§7§lKlucz do CELI";

    @EventHandler
    public void onEndPortalInteraction (PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        Material clicked = event.getClickedBlock().getType();
        ItemStack hand = player.getItemInHand();

        if (clicked == Material.ENDER_PORTAL_FRAME){

            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                event.setCancelled(true);
                return;
            }

            if (hand.getType() == Material.EYE_OF_ENDER){

                Block block = event.getClickedBlock();
                Location loc = block.getLocation();

                player.sendMessage(loc.getBlockX() + ":" + loc.getBlockZ());
                player.sendMessage((loc.getBlockX() - 3) + ":" + loc.getBlockZ());
                player.sendMessage((loc.getBlockX() + 3) + ":" + loc.getBlockZ());

                if ((loc.getBlockX() - 3) % 22 == 0 || loc.getBlockX() - 3 == 0){
                    loc.getBlock().setType(Material.AIR);
                    block.getWorld().playEffect(block.getLocation(), Effect.EXPLOSION_HUGE, 6);
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Odkrywasz, ze Twoja cela posiada piwnice..."));
                }
                else if ((loc.getBlockX() + 3) % 22 == 0 || loc.getBlockX() + 3 == 0){
                    loc.getBlock().setType(Material.BEDROCK);
                    loc.add(0, 4, 0).getBlock().setType(Material.AIR);
                    loc.add(0, 1, 0).getBlock().setType(Material.AIR);
                    block.getWorld().playEffect(block.getLocation(), Effect.EXPLOSION_HUGE, 6);
                    ChatUtil.sendMessage(player, ChatUtil.fixColor("&7Odkrywasz, ze Twoja cela posiada drugie pietro..."));
                }

            }

        }

    }

    @EventHandler
    public void onChestInteraction (PlayerInteractEvent event) {
        if (!event.hasBlock()) return;
        if (!event.getClickedBlock().getWorld().getName().equals("envoy")) return;
        Block b = event.getClickedBlock();
        if (!(b.getState() instanceof Chest)) return;
        Chest chest = (Chest) b.getState();
        EnvoyChest envoyChest = EnvoyChest.get(chest);
        if (envoyChest == null) return;
        if (envoyChest.hasBeenOpened()) return;
        envoyChest.spawnItems(chest);
        envoyChest.setHasBeenOpened(true);
    }

}
