package pl.trollcraft.util;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.trollcraft.Main;
import pl.trollcraft.util.enchants.EnchantRegister;

public class FlyManager {

    public static void listen() {

        final World world = Bukkit.getWorld("world");

        new BukkitRunnable() {

            @Override
            public void run() {

                ItemStack is;
                for (Player player : world.getPlayers()) {

                    if (player.getGameMode() == GameMode.CREATIVE) continue;
                    if (!player.isOnline()) continue;

                    is = player.getInventory().getBoots();
                    if (is == null) {
                        if (player.getAllowFlight()) {
                            player.setAllowFlight(false);
                            player.setFlying(false);
                            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cNie mozesz juz lata."));
                        }
                        continue;
                    }

                    if (is.getEnchantmentLevel(EnchantRegister.FLY_ENCHANTMENT) == 1) {
                        if (!player.getAllowFlight()) {
                            player.setAllowFlight(true);
                            player.setFlying(true);
                            ChatUtil.sendMessage(player, ChatUtil.fixColor("&aMozesz latac!"));
                        }
                    } else {
                        if (player.getAllowFlight()) {
                            player.setAllowFlight(false);
                            player.setFlying(false);
                            ChatUtil.sendMessage(player, ChatUtil.fixColor("&cNie mozesz juz latac."));
                        }
                    }
                }
            }

        }.runTaskTimer(Main.getInstance(), 20, 20);

    }

}
