package minggrosfou.eldoria.controller;

import minggrosfou.eldoria.World;

public class World_Map {
    public int[][] heights;
    public World.Biome[][] biomes;

    public World_Map(World world, int stride) {
        heights = new int[World.max_x_km / stride][World.max_y_km / stride];
        biomes = new World.Biome[World.max_x_km / stride][World.max_y_km / stride];
        for (int i = 0; i < heights.length; i++) {
            for (int j = 0; j < heights[0].length; j++) {
                heights[i][j] = world.heightmap_km[i * stride][j * stride];
                biomes[i][j] = world.biomes_km[i * stride][j * stride];
            }
        }
    }
}
