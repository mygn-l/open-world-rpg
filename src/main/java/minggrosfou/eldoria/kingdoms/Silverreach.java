package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;

public class Silverreach {
    public static int desired_halfwidth = 50;
    public static int capital_length = 5;
    public static World.Biome biome = World.Biome.RIVER;

    public Silverreach(World world) {
        int[] capital_coord = world.get_free_location_in_biome(biome, 0, desired_halfwidth);
        for (int x = capital_coord[0] - desired_halfwidth; x <= capital_coord[0] + desired_halfwidth; x++) {
            for (int y = capital_coord[1] - desired_halfwidth; y <= capital_coord[1] + desired_halfwidth; y++) {
                if (world.kingdom_belongings_km[x][y] == null) {
                    world.kingdom_belongings_km[x][y] = World.Kingdom.SILVERREACH;
                }
            }
        }
    }
}
