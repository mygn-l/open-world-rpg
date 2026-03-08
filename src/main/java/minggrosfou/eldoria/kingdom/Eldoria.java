package minggrosfou.eldoria.kingdom;

import minggrosfou.eldoria.world.Earth;
import minggrosfou.eldoria.city.Luminara;
import minggrosfou.eldoria.city.Willowshade;

public class Eldoria {
    public static int kingdom_halfwidth_km = 300;
    public Luminara luminara;
    public Willowshade willowshade;

    public Eldoria(Earth earth) {
        luminara = new Luminara(earth);

        for (int x = luminara.coord_km[0] - kingdom_halfwidth_km; x <= luminara.coord_km[0] + kingdom_halfwidth_km; x++) {
            for (int y = luminara.coord_km[1] - kingdom_halfwidth_km; y <= luminara.coord_km[1] + kingdom_halfwidth_km; y++) {
                if (earth.biomes_km[x][y] == Earth.Biome.TEMPERATE_GRASSLAND && earth.kingdom_km[x][y] == null) {
                    earth.kingdom_km[x][y] = Earth.Kingdom.ELDORIA;
                }
            }
        }

        willowshade = new Willowshade(luminara);

        System.out.println("Eldoria and Willowshade spawned");
    }
}
