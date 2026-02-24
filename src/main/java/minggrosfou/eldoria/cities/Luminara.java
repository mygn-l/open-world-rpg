package minggrosfou.eldoria.cities;

import minggrosfou.eldoria.World;

public class Luminara {
    public static int halfwidth = 0;
    public int[] coord;

    public Luminara(World world, int desired_halfwidth) {
        coord = world.get_free_location_in_biome(World.Biome.TEMPERATE_GRASSLAND, halfwidth, desired_halfwidth);
    }
}
