package pl.trollcraft.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.trollcraft.Main;
import pl.trollcraft.obj.FortuneData;
import pl.trollcraft.obj.Item;

import java.util.HashMap;

public class DropManager {

    private static HashMap<Integer, Item> drops = new HashMap<>();

    public static ItemStack getItem(int typeId, byte data, int fortuneLvl) {
        int item = typeId + data;
        if (drops.containsKey(item)) {

            Item it = drops.get(item);
            ItemStack itemStack = it.toItemStack();
            HashMap<Integer, FortuneData> fortuneData = it.getFortuneData();
            if (fortuneData == null) return itemStack;

            if (fortuneData.containsKey(fortuneLvl)){
                FortuneData fd = fortuneData.get(fortuneLvl);
                Bukkit.getConsoleSender().sendMessage("Szczescie " + fortuneLvl);
                itemStack.setAmount(fd.getAmount());
            }

            return itemStack;

        }
        return null;
    }

    public static void load() {

        YamlConfiguration conf = Configs.load("drops.yml", Main.getInstance());

        conf.getConfigurationSection("drops").getKeys(false).forEach( s -> {
            Material brokenMaterial = Material.getMaterial(conf.getString("drops." + s + ".broken.material"));
            byte brokenData = Byte.parseByte(conf.getString("drops." + s + ".broken.data"));

            int brokenCode = brokenMaterial.ordinal() + brokenData;

            Material gotMaterial = Material.getMaterial(conf.getString("drops." + s + ".got.material"));
            byte gotData = Byte.parseByte(conf.getString("drops." + s + ".got.data"));

            Item item;
            if (conf.contains("drops." + s + ".got.fortune")){

                HashMap<Integer, FortuneData> fortuneData = new HashMap<>();
                conf.getStringList("drops." + s + ".got.fortune").forEach( e -> {

                    String[] fd = e.split(":");
                    int lvl = Integer.parseInt(fd[0]);
                    String[] amounts = fd[1].split("-");
                    int min = Integer.parseInt(amounts[0]);
                    int max = Integer.parseInt(amounts[1]);

                    fortuneData.put(lvl, new FortuneData(min, max));

                } );
                item = new Item(gotMaterial, gotData, fortuneData);
            }
            else
                item = new Item(gotMaterial, gotData);

            drops.put(brokenCode, item);
        } );

    }

}
