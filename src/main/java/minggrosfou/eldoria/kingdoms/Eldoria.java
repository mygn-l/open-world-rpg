package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;
import minggrosfou.eldoria.cities.Luminara;
import minggrosfou.eldoria.cities.Willowshade;

public class Eldoria {
    public static int desired_halfwidth = 300;
    public static World.Biome biome = World.Biome.TEMPERATE_GRASSLAND;
    public static int[] capital_coord;
    public Willowshade willowshade;
    public Luminara luminara;

    public Eldoria(World world) {
        luminara = new Luminara(world, desired_halfwidth);

        for (int x = luminara.coord[0] - desired_halfwidth; x <= luminara.coord[0] + desired_halfwidth; x++) {
            for (int y = luminara.coord[1] - desired_halfwidth; y <= luminara.coord[1] + desired_halfwidth; y++) {
                if (world.biomes_km[x][y] == World.Biome.TEMPERATE_GRASSLAND && world.kingdom_belongings_km[x][y] == null) {
                    world.kingdom_belongings_km[x][y] = World.Kingdom.ELDORIA;
                }
            }
        }

        willowshade = new Willowshade(world, this);
    }
}
