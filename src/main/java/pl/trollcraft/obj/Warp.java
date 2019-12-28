package pl.trollcraft.obj;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import pl.trollcraft.Main;
import pl.trollcraft.util.ChatUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Warp {

    private static ArrayList<Warp> warps = new ArrayList<Warp>();

    private String name;
    private Location location;

    public Warp(String name, Location location) {
        this.name = name;
        this.location = location;
        warps.add(this);
    }

    public void teleport(Player player) {
        if (!location.getChunk().isLoaded()) location.getChunk().load();
        player.teleport(location);
    }

    public void save() {

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO prison_warps VALUES (");
        query.append("'" + name + "', '" + ChatUtil.locToString(location) + "')");

        try {
            Connection conn = Main.getDatabaseHandler().openConnection();
            conn.prepareStatement(query.toString()).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Warp get(String name) {
        for (Warp w : warps){
            if (w.name.equals(name)) return w;
        }
        return null;
    }

    public static void load() {

        try {
            Connection conn = Main.getDatabaseHandler().openConnection();
            ResultSet rs = conn.prepareStatement("SELECT * FROM prison_warps").executeQuery();

            String name;
            Location loc;
            while (rs.next()){
                name = rs.getString("name");
                loc = ChatUtil.locFromString(rs.getString("location"));
                new Warp(name, loc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
