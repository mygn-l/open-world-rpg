package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;

public class Beastkin_Confederacy {
    public static int desired_halfwidth = 300;
    public static int capital_halfwidth = 20;
    public static World.Biome biome = World.Biome.TROPICAL;

    public Beastkin_Confederacy(World world) {
        int[] capital_coord = world.get_free_location_in_biome(biome, capital_halfwidth, desired_halfwidth);
        for (int x = capital_coord[0] - desired_halfwidth; x <= capital_coord[0] + desired_halfwidth; x++) {
            for (int y = capital_coord[1] - desired_halfwidth; y <= capital_coord[1] + desired_halfwidth; y++) {
                if (world.biomes_km[x][y] == biome && world.kingdom_belongings_km[x][y] == null) {
                    world.kingdom_belongings_km[x][y] = World.Kingdom.BEASTKIN_CONFEDERACY;
                }
            }
        }
    }
}
