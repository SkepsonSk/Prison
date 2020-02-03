package pl.trollcraft.obj.cells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.trollcraft.Main;
import pl.trollcraft.util.Configs;
import pl.trollcraft.util.Debug;
import pl.trollcraft.util.SchematicUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class CellNode {

    private static ArrayList<CellNode> cellNodes = new ArrayList<>();

    private static int nextId;

    private int id;
    private int x;
    private int z;

    // 0 - id, 1 - x, 2 - z
    private ArrayList<CellData> connections;

    public CellNode(int id, int x, int z) {
        this.id = id;
        this.x = x;
        this.z = z;
        connections = new ArrayList<>();
        cellNodes.add(this);
    }

    public CellNode(int x, int z) {
        this.id = nextId;
        this.x = x;
        this.z = z;
        connections = new ArrayList<>();
        cellNodes.add(this);
        nextId++;
    }

    public void hookConnections() {
        connections.clear();
        int[] xPos = new int[2];
        int[] zPos = new int[2];
        xPos[0] = x + 22;
        xPos[1] = x - 22;
        zPos[0] = z + 22;
        zPos[1] = z - 22;
        CellNode a = getCellNode(xPos[0], z);
        CellNode b = getCellNode(xPos[1], z);
        CellNode c = getCellNode(x, zPos[0]);
        CellNode d = getCellNode(x, zPos[1]);

        if (a != null)
            connections.add(new CellData(a.id, a.x, a.z));
        if (b != null)
            connections.add(new CellData(b.id, b.x, b.z));
        if (c != null)
            connections.add(new CellData(c.id, c.x, c.z));
        if (d != null)
            connections.add(new CellData(d.id, d.x, d.z));

    }

    public void createCorridors(World world) {
        int y = 90, mid;
        Location loc;
        boolean rotate = false;

        for (CellData cd : connections) {

            if (cd.getX() == x) {
                mid = (cd.getZ() + z) / 2;
                loc = new Location(world, x, y, mid);
                Debug.log("CORR: N-S");
                rotate = true;
            }
            else{
                mid = (cd.getX() + x) / 2;
                loc = new Location(world, mid, y, z);
                Debug.log("CORR: E-W");
            }

            File corridorFile;
            if (rotate) {
                corridorFile = new File(Main.getInstance().getDataFolder(), "corridor_ns.schematic");
                SchematicUtils.pasteSchematic(corridorFile, loc, 90);
            }
            else {
                corridorFile = new File(Main.getInstance().getDataFolder(), "corridor_ew.schematic");
                SchematicUtils.pasteSchematic(corridorFile, loc);
            }
        }
    }

    public void updateConnected() {
        CellNode cn;
        for (CellData pos : connections) {
            cn = getCellNode(pos.getX(), pos.getZ());
            if (cn == null) continue;
            cn.hookConnections();
        }
    }

    @Override
    public String toString() {
        String text = "Cela " + id + ":\n";
        text += "X: " + x + "\n";
        text += "Z: " + z + "\n";
        text += "Polaczenia:\n";
        for (CellData cd : connections)
            text += cd.getId() + ", ";
        return text;
    }

    public int[] getFreeDirection() {

        if (connections.size() == 4) return null;
        if (connections.size() == 0) return new int[] {x + 22, z};

        int[] xPos = new int[2];
        int[] zPos = new int[2];
        xPos[0] = x + 22;
        xPos[1] = x - 22;
        zPos[0] = z + 22;
        zPos[1] = z - 22;

        for (int i = 0 ; i < xPos.length ; i++) {
            for (CellData conn : connections){
                if (xPos[i] == conn.getX())
                    xPos[i] = 1;
                else if (zPos[i] == conn.getZ())
                    zPos[i] = 1;
            }
        }

        if (xPos[0] != 1) return new int[] {xPos[0], z};
        else if (xPos[1] != 1) return new int[] {xPos[1], z};
        else if (zPos[0] != 1) return new int[] {x, zPos[0]};
        else if (zPos[1] != 1) return new int[] {x, zPos[1]};

        return null;
    }

    public static void load() {
        YamlConfiguration conf = Configs.load("cellsgenerator.yml", Main.getInstance());

        nextId = conf.getInt("next_id");
        Set<String> cells = conf.getConfigurationSection("cellnodes").getKeys(false);

        Debug.log("&cLoading cells...");
        for (String s : cells){
            int id = Integer.parseInt(s);
            int x = conf.getInt(String.format("cellnodes.%s.x", s));
            int z = conf.getInt(String.format("cellnodes.%s.z", s));
            ArrayList<CellData> conns = new ArrayList<>();

            for (int nId : conf.getIntegerList(String.format("cellnodes.%s.connections", s))){
                conns.add(new CellData(nId, 0, 0));
            }

            new CellNode(id, x, z).connections = conns;
        }

        Debug.log("&cConnecting cells...");
        for (CellNode cn : cellNodes) {
            Debug.log("&cConnecting cell...");
            for (CellData conn : cn.connections) {
                int[] pos = getPositions(conn.getId());
                conn.setX(pos[0]);
                conn.setZ(pos[1]);
                Debug.log("&c" + pos[0] + ":" + pos[1]);
                Debug.log("&c" + conn.getX() + ":" + conn.getZ());
            }
        }

        Collections.shuffle(cellNodes);
    }

    public static ArrayList<CellNode> getCellNodes() { return cellNodes; }

    public static boolean noCells() {
        return cellNodes.isEmpty();
    }

    public static int[] getPositions(int id) {
        for (CellNode cn : cellNodes) {
            if (cn.id == id) return new int[] {cn.x, cn.z};
        }
        return null;
    }

    public static CellNode getCellNode(int x, int z) {
        for (CellNode cn : cellNodes) {
            if (cn.x == x && cn.z == z) return cn;
        }
        return null;
    }

    public static int[] getPositionForNewCell() {
        for (CellNode cn : cellNodes) {
            int[] pos = cn.getFreeDirection();
            if (pos != null)
                return pos;
        }
        return null;
    }

    public static void saveAll() {
        YamlConfiguration conf = Configs.load("cellsgenerator.yml", Main.getInstance());
        conf.set("cellnodes", null);
        for (CellNode cn : cellNodes) {
            Debug.log("&aZapis: " + cn.id + " (" + cn.x + ", " + cn.z + ")");

            conf.set(String.format("cellnodes.%d.x", cn.id), cn.x);
            conf.set(String.format("cellnodes.%d.z", cn.id), cn.z);
            ArrayList<Integer> cellsConns = new ArrayList<>();
            cn.connections.forEach( cellData -> cellsConns.add(Integer.valueOf(cellData.getId())) );
            conf.set(String.format("cellnodes.%d.connections", cn.id), cellsConns);
        }
        conf.set("next_id", nextId);
        Configs.save(conf, "cellsgenerator.yml");
    }

}
