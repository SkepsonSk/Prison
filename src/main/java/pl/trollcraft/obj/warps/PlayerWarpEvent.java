package pl.trollcraft.obj.warps;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWarpEvent extends Event {

    private Player player;
    private Warp warp;

    public PlayerWarpEvent (Player player, Warp warp) {
        this.player = player;
        this.warp = warp;
    }

    public Player getPlayer() { return player; }
    public Warp getWarp() { return warp; }

    // -------- -------- -------- --------

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }

}
