package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;

public class Azureport_Republic {
    public static int desired_halfwidth = 100;
    public static int capital_length = 25;
    public static World.Biome biome = World.Biome.COASTAL;

    public Azureport_Republic(World world) {
        int[] capital_coord = world.get_free_location_in_biome(biome, 0, desired_halfwidth);
        while (capital_coord[1] < World.max_y_km * 0.3 || capital_coord[1] > World.max_y_km * 0.7) {
            capital_coord = world.get_free_location_in_biome(biome, 0, desired_halfwidth);
        }
        for (int x = capital_coord[0] - desired_halfwidth; x <= capital_coord[0] + desired_halfwidth; x++) {
            for (int y = capital_coord[1] - desired_halfwidth; y <= capital_coord[1] + desired_halfwidth; y++) {
                if (world.kingdom_belongings_km[x][y] == null) {
                    world.kingdom_belongings_km[x][y] = World.Kingdom.AZUREPORT_REPUBLIC;
                }
            }
        }
    }
}
