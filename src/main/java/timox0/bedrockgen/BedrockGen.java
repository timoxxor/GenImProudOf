package timox0.bedrockgen;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.util.Random;
import java.util.logging.Level;

public final class BedrockGen extends JavaPlugin {
    public static File file;
    public static BedrockGen plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new SelectionListener(), this);
        this.getCommand("saveBebra").setExecutor(new Command());
        this.getCommand("loadBebra").setExecutor(new Command2());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        getLogger().log(Level.WARNING, "CustomChunkGenerator is used!");
        PerlinNoiseGenerator noiseGenerator = new PerlinNoiseGenerator(new Random().nextLong());
        StructureGenerator structureGenerator = new StructureGenerator();
        return new CustomWorldGenerator(noiseGenerator, structureGenerator); // Return an instance of the chunk generator we want to use.
    }
}
