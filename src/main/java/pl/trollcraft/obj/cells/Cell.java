package pl.trollcraft.obj.cells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private static ArrayList<Cell> cells = new ArrayList<>();

    private Player player;
    private Location location;
    private List<String> allowedPlayers;
    private List<Integer> visitors;

    public Cell(Player player, Location location, List<String> allowedPlayers) {
        this.player = player;
        this.location = location;
        this.allowedPlayers = allowedPlayers;
        visitors = new ArrayList<>();
        cells.add(this);
    }

    public Player getPlayer() { return player; }
    public Location getLocation() { return location; }

    public void teleport() { player.teleport(location); }
    public void teleport(Player p) { p.teleport(location); }

    public void save() {
        YamlConfiguration conf = Configs.load("cells.yml", Main.getInstance());
        conf.set(String.format("cells.%s.location.x", player.getName()), location.getBlockX());
        conf.set(String.format("cells.%s.location.y", player.getName()), location.getBlockY());
        conf.set(String.format("cells.%s.location.z", player.getName()), location.getBlockZ());
        conf.set(String.format("cells.%s.allowedPlayers", player.getName()), allowedPlayers);
        Configs.save(conf, "cells.yml");
    }

    public void unload() { cells.remove(this); }

    public static void load(Player player) {
        if (get(player) != null) return;

        YamlConfiguration conf = Configs.load("cells.yml", Main.getInstance());
        if (!conf.contains(String.format("cells.%s", player.getName()))) return;

        int x = conf.getInt(String.format("cells.%s.location.x", player.getName()));
        int y = conf.getInt(String.format("cells.%s.location.y", player.getName()));
        int z = conf.getInt(String.format("cells.%s.location.z", player.getName()));
        Location location = new Location(Bukkit.getWorld("cells"), x, y, z);

        List<String> allowedPlayers = conf.getStringList(String.format("cells.%s.allowedPlayers", player.getName()));

        new Cell(player, location, allowedPlayers);

        player.sendMessage("Cela zaladowana.");
    }

    public static Cell get(Player player) {
        for (Cell cell : cells) {
            if (cell.player.getEntityId() == player.getEntityId()) return cell;
        }
        return null;
    }

}
