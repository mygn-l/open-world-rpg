package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;

public class Desert_Stop {
    public static int desert_depth = 100;
    public static World.Biome biome = World.Biome.DESERT;

    public Desert_Stop(World world) {
        int[] capital_coord = world.get_free_location_in_biome(biome, desert_depth, 0);
        world.kingdom_belongings_km[capital_coord[0]][capital_coord[1]] = World.Kingdom.DESERT_STOP;
    }
}
