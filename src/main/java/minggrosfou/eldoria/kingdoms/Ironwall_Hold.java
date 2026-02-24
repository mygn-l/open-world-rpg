package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;

public class Ironwall_Hold {
    public static int desired_halfwidth = 200;
    public static int capital_halfwidth = 15;
    public static World.Biome biome = World.Biome.TUNDRA;

    public Ironwall_Hold(World world) {
        int[] capital_coord = world.get_free_location_in_biome(biome, capital_halfwidth, desired_halfwidth);
        for (int x = capital_coord[0] - desired_halfwidth; x <= capital_coord[0] + desired_halfwidth; x++) {
            for (int y = capital_coord[1] - desired_halfwidth; y <= capital_coord[1] + desired_halfwidth; y++) {
                if (world.biomes_km[x][y] == biome && world.kingdom_belongings_km[x][y] == null) {
                    world.kingdom_belongings_km[x][y] = World.Kingdom.IRONWALL_HOLD;
                }
            }
        }
    }
}
