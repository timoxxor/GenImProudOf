package timox0.bedrockgen;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static timox0.bedrockgen.BedrockGen.plugin;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (player.isOp()){
                if (strings == null) return false;
                String arg = strings[0];
                assert SelectionListener.getStartPos() != null;
                assert SelectionListener.getEndPos() != null;
                BoundingBox boundingBox = BoundingBox.of(SelectionListener.getStartPos(), SelectionListener.getEndPos());
                Box box = new Box<Material>((int)boundingBox.getWidthX(), (int)boundingBox.getHeight(),
                        (int)boundingBox.getWidthZ(), (x, y, z) -> {
                            return ((Player) commandSender).getWorld().getBlockAt((int)boundingBox.getMinX()+x,
                                    (int)boundingBox.getMinY()+y, (int)boundingBox.getMinZ()+z).getType();
                        });
                try {
                    Bukkit.getServer().getLogger().log(Level.WARNING, arg);
                    box.saveToFile(new File(plugin.getDataFolder(), arg));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return false;
    }
}
