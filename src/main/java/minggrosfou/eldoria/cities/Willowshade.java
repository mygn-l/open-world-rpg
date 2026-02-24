package minggrosfou.eldoria.cities;

import minggrosfou.eldoria.World;
import minggrosfou.eldoria.kingdoms.Eldoria;

public class Willowshade {
    public static int[] world_coord_km;

    //willowshade only occupies a 3km x 3km area, the coords are in meters
    //origin at bottom left, north is positive y, east is positive x
    public static int[] plaza_coord = new int[]{1500, 1500};

    public Willowshade(World world, Eldoria eldoria) {
        world_coord_km = new int[]{eldoria.luminara.coord[0] - 200, eldoria.luminara.coord[1] + 100};
        /*
        while (
                world.kingdom_belongings_km[world_coord_km[0]][world_coord_km[1]] != World.Kingdom.ELDORIA ||
                        world.biomes_km[world_coord_km[0]][world_coord_km[1]] != World.Biome.TEMPERATE_GRASSLAND
        ) {
            world_coord_km[0] += 5;
            world_coord_km[1] += 5;
        }
        */
    }
}
