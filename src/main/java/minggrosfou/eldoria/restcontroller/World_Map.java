package minggrosfou.eldoria.restcontroller;

import minggrosfou.eldoria.world.Earth;

public class World_Map {
    public int[][] heights;
    public Earth.Biome[][] biomes;

    public World_Map(Earth earth, int stride) {
        heights = new int[Earth.max_x_km / stride][Earth.max_y_km / stride];
        biomes = new Earth.Biome[Earth.max_x_km / stride][Earth.max_y_km / stride];
        for (int i = 0; i < heights.length; i++) {
            for (int j = 0; j < heights[0].length; j++) {
                heights[i][j] = earth.heightmap_km[i * stride][j * stride];
                biomes[i][j] = earth.biomes_km[i * stride][j * stride];
            }
        }
    }
}
