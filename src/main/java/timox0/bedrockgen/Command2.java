package timox0.bedrockgen;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.logging.Level;

import static timox0.bedrockgen.BedrockGen.plugin;

public class Command2 implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (player.isOp()){
                if (strings == null) return false;
                String arg = strings[0];
                try {
                    Bukkit.getServer().getLogger().log(Level.WARNING, arg);
                    Box<BlockInfo> box = (Box<BlockInfo>) Box.loadFormFile(new File(plugin.getDataFolder(), arg));
                    box.build((x, y, z, value) -> player.getWorld().setBlockData(player.getLocation().add(x, y, z), value.getBlockData()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return false;
    }
}
