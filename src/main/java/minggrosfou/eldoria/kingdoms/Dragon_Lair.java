package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;

public class Dragon_Lair {
    public static World.Biome biome = World.Biome.NIVAL;

    public Dragon_Lair(World world) {
        int[] capital_coord = world.get_free_location_in_biome(biome, 0, 0);
        world.kingdom_belongings_km[capital_coord[0]][capital_coord[1]] = World.Kingdom.DRAGON_LAIR;
    }
}
