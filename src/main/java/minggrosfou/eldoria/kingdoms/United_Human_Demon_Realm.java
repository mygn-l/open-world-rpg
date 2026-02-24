package minggrosfou.eldoria.kingdoms;

import minggrosfou.eldoria.World;

public class United_Human_Demon_Realm {
    public United_Human_Demon_Realm(World world, int x, int y) {
        for (int i = x - World.mountain_width_km; i < x + World.mountain_width_km; i++) {
            for (int j = y - World.mountain_width_km; j < y + World.mountain_width_km; j++) {
                world.kingdom_belongings_km[i][j] = World.Kingdom.UNITED_HUMAN_DEMON_REALM;
            }
        }
    }
}
