package timox0.bedrockgen;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectionListener implements Listener {
    private static Location startPos;
    private static Location endPos;
    @EventHandler
    public void playerInteract(PlayerInteractEvent e){
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.GOLDEN_AXE) return;
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        if (!(e.getPlayer().isOp())) return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
            endPos = e.getClickedBlock().getLocation();

            e.getPlayer().sendRawMessage( ChatColor.DARK_PURPLE + "EndPos: " + endPos);
            e.setCancelled(true);
        }
        if (e.getAction() == Action.LEFT_CLICK_BLOCK){
            startPos = e.getClickedBlock().getLocation();

            e.getPlayer().sendRawMessage( ChatColor.DARK_PURPLE + "StartPos: " + startPos);
            e.setCancelled(true);
        }
    }

    public static Location getEndPos() {
        if (endPos == null) return null;

        return endPos;
    }

    public static Location getStartPos() {
        if (startPos == null) return null;

        return startPos;
    }
}
