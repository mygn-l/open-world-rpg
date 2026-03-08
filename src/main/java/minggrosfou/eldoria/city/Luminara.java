package minggrosfou.eldoria.city;

import minggrosfou.eldoria.world.Earth;

public class Luminara {
    public static int city_halfwidth_km = 0;
    public int[] coord_km;

    public Luminara(Earth earth) {
        coord_km = earth.get_free_location_in_biome(Earth.Biome.TEMPERATE_GRASSLAND, city_halfwidth_km);
    }
}
