package timox0.bedrockgen;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

import java.io.Serializable;

public class BlockInfo implements Serializable {
    static final long serialVersionUID = 33L;

    private String data;

    public BlockInfo(Block block) {
        data = block.getBlockData().getAsString();
    }

    public BlockData getBlockData() {
        return Bukkit.getServer().createBlockData(data);
    }
}