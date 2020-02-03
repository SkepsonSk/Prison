package pl.trollcraft.obj.cells;

public class CellData {

    private int id, x,z;

    public CellData(int id, int x, int z){
        this.id = id;
        this.x = x;
        this.z = z;
    }

    public int getId() { return id; }
    public int getX() { return x; }
    public int getZ() { return z; }

    public void setX(int x) { this.x = x; }
    public void setZ(int z) { this.z = z; }

}
