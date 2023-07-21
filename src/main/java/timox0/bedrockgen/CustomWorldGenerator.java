package timox0.bedrockgen;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class  CustomWorldGenerator extends ChunkGenerator {

    public CustomWorldGenerator(PerlinNoiseGenerator noiseGenerator, StructureGenerator structureGenerator) {
        noise = noiseGenerator;
        CustomWorldGenerator.structureGenerator = structureGenerator;
    }

    private static PerlinNoiseGenerator noise;
    private static StructureGenerator structureGenerator;
    private static final int CAVE_RADIUS = 3;
    private static final double NOISE_FREQUENCY = 0.2;
    private static final double CAVE_DENSITY = 0.6;
    private static final double WORM_DENSITY = 0.3;
    private static final double WORM_LENGTH = 9;
    private static final double WORM_RADIUS = 3;
    private static final int maxHeight = 256;
    private static final int minHeight = 0;
    public static List<Location> locationList = new ArrayList<>();

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Loop through all block positions in the chunk
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Calculate the global coordinates of the current block position
                int globalX = chunkX * 16 + x;
                int globalZ = chunkZ * 16 + z;

                for (int y = 0; y < 256; y++) {
                    chunkData.setBlock(x, y, z, Material.BEDROCK);

                    double noiseValue = noise.noise(globalX * NOISE_FREQUENCY, y * NOISE_FREQUENCY, globalZ * NOISE_FREQUENCY);

                    // Check if the current position should be a cave
                    if (noiseValue > CAVE_DENSITY && Math.pow(x - 8, 2) + Math.pow(z - 8, 2) <= Math.pow(CAVE_RADIUS, 2)) {
                        // Set the block to air
                        chunkData.setBlock(x, y, z, Material.AIR);
                    } else if (y > 0 && chunkData.getType(x, y - 1, z) == Material.AIR) {
                        // Interpolate between cave and solid ground
                        double interpolationFactor = (noiseValue - CAVE_DENSITY) / (1.0 - CAVE_DENSITY);
                        int groundHeight = findGroundHeight(chunkData, x, y, z);
                        if (y < groundHeight) {
                            double blend = interpolate(groundHeight - y, 0, groundHeight, 0, 1);
                            double smoothInterpolation = smoothstep(blend);
                            if (interpolationFactor > smoothInterpolation) {
                                chunkData.setBlock(x, y, z, Material.AIR);
                            }
                        }
                    }

                    // Check if the current position should be a worm
                    if (noiseValue > WORM_DENSITY) {
                        int wormLength = (int) (WORM_LENGTH + (noiseValue - WORM_DENSITY) * 10);
                        double wormRadius = Math.max(1, WORM_RADIUS * noiseValue);

                        for (int i = 0; i < wormLength; i++) {
                            double dx = noise.noise((globalX + i) * NOISE_FREQUENCY, y * NOISE_FREQUENCY, globalZ * NOISE_FREQUENCY);
                            double dy = noise.noise(globalX * NOISE_FREQUENCY, (y - i) * NOISE_FREQUENCY, globalZ * NOISE_FREQUENCY);
                            double dz = noise.noise(globalX * NOISE_FREQUENCY, y * NOISE_FREQUENCY, (globalZ + i) * NOISE_FREQUENCY);

                            // Set the block to air if it's within the radius of the worm
                            if (Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2) <= Math.pow(wormRadius, 2)) {
                                chunkData.setBlock(x + i, y - i, z + i, Material.AIR);
                            }
                        }
                    }
                }
            }

        }

        // GigaChad box
        Random random1 = new Random(chunkX/32+chunkZ/32*100L);
        int rad = random1.nextInt(4);
        boolean ae = (rad == 0);
        if (ae){
            int a = chunkX/16 % 2;
            int b = chunkZ/16 % 2;

            int posX = random1.nextInt(8);
            int posY = random1.nextInt(256);
            int posZ = random1.nextInt(8);

            int boxSize = 24;

            for (int x = a == 0 ? posX : 0; x < (a == 0 ? 16 : posX + boxSize);x++ ){
                for (int z = b == 0 ? posZ : 0; z < (b == 0 ? 16 : posZ + boxSize);z++ ){
                    for (int y = posY; y < (posY+boxSize); y++ ){
                        chunkData.setBlock(x,y,z, Material.AIR);
                    }
                }
            }

        } else {
            for (char i = 0; i < 2; i++) {
                // Carve box out
                int posX = random.nextInt(9);
                int posY = random.nextInt(256);
                int posZ = random.nextInt(9);

                int boxSize = random.nextInt(3) + 6 ;

                World world = Bukkit.getServer().getWorld("world");

                Location corner0 = new Location(world, posX, posY, posZ);
                Location corner1 = new Location(world, posX + boxSize, posY, posZ);
                Location corner2 = new Location(world, posX + boxSize, posY, posZ + boxSize);
                Location corner3 = new Location(world, posX, posY, posZ + boxSize);

                int index = random.nextInt(5);

                for (int x = posX; x < (posX + boxSize); x++) {
                    for (int y = posY; y < (posY + boxSize); y++) {
                        for (int z = posZ; z < (posZ + boxSize); z++) {
                            chunkData.setBlock(x, y, z, Material.AIR);
                        }
                    }
                }

                int pit = random.nextInt(2) + 1;
                Location pitLoc = new Location(world, posX + pit, posY, posZ + pit );

                if (index == 0) {
                    structureGenerator.generateStructure(chunkData, corner0 ,index);
                } else if (index == 1) {
                    structureGenerator.generateStructure(chunkData, corner1 ,index);
                } else if (index == 2) {
                    structureGenerator.generateStructure(chunkData, corner2 ,index);
                } else if (index == 3){
                    structureGenerator.generateStructure(chunkData, corner3 ,index);
                } else{
                    structureGenerator.generateStructure(chunkData, pitLoc , index);
                }

                Location[] corners = {corner0, corner1, corner2, corner3};
                for (int j = 0; j < 3; j++) {
                    int cobwebCornerIndex = random.nextInt(4); // Choose a random corner
                    Location cobwebLocation = corners[cobwebCornerIndex].clone().add(cobwebCornerIndex ==1 || cobwebCornerIndex ==2 ? -1:0,
                            boxSize-1, cobwebCornerIndex >= 2 ? -1:0);
                    chunkData.setBlock(cobwebLocation.getBlockX(), cobwebLocation.getBlockY(), cobwebLocation.getBlockZ(), Material.COBWEB);
                }
            }
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 255; y < worldInfo.getMaxHeight(); y++) {
                    chunkData.setBlock(x, y, z, Material.BEDROCK);
                }
                for (int y = 0; y > worldInfo.getMinHeight(); y--) {
                    chunkData.setBlock(x, y, z, Material.BEDROCK);
                }
            }
        }
    }

    private int findGroundHeight(ChunkData chunkData, int x, int y, int z) {
        for (int i = y; i >= 0; i--) {
            if (chunkData.getType(x, i, z) != Material.AIR) {
                return i;
            }
        }
        return 0;
    }

    private double interpolate(double value, double minSrc, double maxSrc, double minDst, double maxDst) {
        return minDst + (value - minSrc) * (maxDst - minDst) / (maxSrc - minSrc);
    }

    private double smoothstep(double t) {
        return t * t * (3 - 2 * t);
    }
}