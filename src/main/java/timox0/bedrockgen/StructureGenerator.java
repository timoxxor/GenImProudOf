package timox0.bedrockgen;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static timox0.bedrockgen.BedrockGen.plugin;

public class StructureGenerator {
    List<Box<BlockData>> boxes = new ArrayList<>();
    List<Box<BlockData>> pitBoxes = new ArrayList<>();
    File[] files;

    public StructureGenerator() {
        File folder = plugin.getDataFolder();
        files = folder.listFiles();
        for (File file : files) {
            if (file.getName().contains("corner")) {
                try {
                    Box<BlockData> box = ((Box<BlockInfo>) Box.loadFormFile(file)).transform(BlockInfo::getBlockData);
                    boxes.add(box);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (File file : files){
            if (file.getName().contains("pit")){
                try {
                    Box<BlockData> box = ((Box<BlockInfo>) Box.loadFormFile(file)).transform(BlockInfo::getBlockData);
                    ;
                    pitBoxes.add(box);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void generateStructure(ChunkGenerator.ChunkData chunkData, Location location, int i){
        int index = new Random().nextInt(boxes.size());
        boolean ind = new Random().nextBoolean();

        if (i == 0){
            boxes.get(index).build((x, y, z, value) -> {
                chunkData.setBlock(location.getBlockX() + x, location.getBlockY() + y - 3, location.getBlockZ() + z, value);
            });
        } else if (i == 1) {
            Box<BlockData> box = boxes.get(index).flipX();
            box.build((x, y, z, value) -> {
                chunkData.setBlock(location.getBlockX() + x - box.sizeX(), location.getBlockY() + y - 3, location.getBlockZ() + z, value);
            });
        } else if (i == 2) {
            Box<BlockData> box = boxes.get(index).flipX().flipZ();
            box.build((x, y, z, value) -> {
                chunkData.setBlock(location.getBlockX() + x - box.sizeX(), location.getBlockY() + y - 3, location.getBlockZ() + z - box.sizeZ(), value);
            });
        } else if (i == 3) {
            Box<BlockData> box = boxes.get(index).flipZ();
            box.build((x, y, z, value) -> {
                chunkData.setBlock(location.getBlockX() + x, location.getBlockY() + y - 3, location.getBlockZ() + z - box.sizeZ(), value);
            });
        } else {
            if (ind) {
                Box<BlockData> box = pitBoxes.get(0);
                box.build((x, y, z, value) -> {
                    chunkData.setBlock(location.getBlockX() + x, location.getBlockY() + y - 3, location.getBlockZ() + z, value);
                });
            } else {
                Box<BlockData> box = pitBoxes.get(1);
                box.build((x, y, z, value) -> {
                    chunkData.setBlock(location.getBlockX() + x, location.getBlockY() + y - 3, location.getBlockZ() + z, value);
                });
            }
        }
    }
}
