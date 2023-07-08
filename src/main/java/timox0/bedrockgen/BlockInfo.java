package timox0.bedrockgen;

public class BlockInfo implements Serializable {
    static final long serialVersionUID = 33L;

    privete String data;

    public BlockInfo(Block block) {
        data = block.getBlockData().getAsString()
    }

    public BlockData getBlockData() {
        Bukkit.getServer().createBlockData(data);
    }
}