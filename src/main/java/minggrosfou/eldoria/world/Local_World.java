package minggrosfou.eldoria.world;

import minggrosfou.eldoria.Custom_Math;
import minggrosfou.eldoria.entity.Entity;

public class Local_World {
    private static final int farsight_distance_m = 3000;
    private static final int nearsight_distance_m = 300;

    //heights in meters
    public int[][][][] farsight_10m_heightmap_km_chunk;
    public double[][][][] nearsight_m_heightmap_100m_chunk;

    public int[] farsight_center_km;
    public int[] nearsight_center_m;

    private final Earth earth;
    private final Entity entity;

    public Local_World(Earth earth, Entity entity) {
        this.earth = earth;
        this.entity = entity;

        generate_all_farsight_near_player();
        generate_all_nearsight_near_player();
    }

    private int[][] generate_farsight_at(int[] world_km) {
        int[][] farsight_heightmap_10m_within_km = new int[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                farsight_heightmap_10m_within_km[i][j] = earth.heightmap_km[world_km[0]][world_km[1]];
            }
        }

        Custom_Math.add_perlin(farsight_heightmap_10m_within_km, 50, world_km[0] * 2, world_km[1] * 2, Earth.max_x_km * 100, 5);
        Custom_Math.add_perlin(farsight_heightmap_10m_within_km, 10, world_km[0] * 10, world_km[1] * 10, Earth.max_x_km * 100, 1);
        Custom_Math.add_perlin(farsight_heightmap_10m_within_km, 2, world_km[0] * 50, world_km[1] * 50, Earth.max_x_km * 100, 0.5);

        return farsight_heightmap_10m_within_km;
    }

    private void generate_all_farsight_near_player() {
        int[] player_world_km = new int[]{(int) (entity.world_cm[0] / 100000), (int) (entity.world_cm[1] / 100000)};

        farsight_center_km = player_world_km;

        farsight_10m_heightmap_km_chunk = new int[farsight_distance_m / 1000 * 2 + 1][farsight_distance_m / 1000 * 2 + 1][100][100];
        for (int i = 0; i < farsight_10m_heightmap_km_chunk.length; i++) {
            for (int j = 0; j < farsight_10m_heightmap_km_chunk[0].length; j++) {
                farsight_10m_heightmap_km_chunk[i][j] = generate_farsight_at(new int[]{player_world_km[0] - farsight_distance_m / 1000 + i, player_world_km[1] - farsight_distance_m / 1000 + j});
            }
        }
    }

    //world_m must be multiple of 100
    private double[][] generate_100x100_nearsight_using_farsight_with_corner(int[] world_m) {
        int[] world_km = new int[]{world_m[0] / 1000, world_m[1] / 1000};
        int[] chunk_index = new int[]{(world_m[0] % 1000) / 10, (world_m[1] % 1000) / 10};

        double[][] nearsight_heightmap_m_within_100m = new double[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                nearsight_heightmap_m_within_100m[i][j] = farsight_10m_heightmap_km_chunk[world_km[0]][world_km[1]][chunk_index[0]][chunk_index[1]];
            }
        }

        Custom_Math.add_perlin(nearsight_heightmap_m_within_100m, 50, world_m[0] / 100 * 2, world_m[1] / 100 * 2, Earth.max_x_km * 1000, 1);
        Custom_Math.add_perlin(nearsight_heightmap_m_within_100m, 10, world_m[0] / 100 * 10, world_m[1] / 100 * 10, Earth.max_x_km * 1000, 0.5);
        Custom_Math.add_perlin(nearsight_heightmap_m_within_100m, 2, world_m[0] / 100 * 50, world_m[1] / 100 * 50, Earth.max_x_km * 1000, 0.1);

        Custom_Math.average_smooth_heightmap(nearsight_heightmap_m_within_100m);

        return nearsight_heightmap_m_within_100m;
    }

    private void generate_all_nearsight_near_player() {
        int[] player_world_m = new int[]{(int) (entity.world_cm[0] / 100), (int) (entity.world_cm[1] / 100)};

        nearsight_center_m = player_world_m;

        nearsight_m_heightmap_100m_chunk = new double[nearsight_distance_m / 100 * 2 + 1][nearsight_distance_m / 100 * 2 + 1][100][100];
        for (int i = 0; i < nearsight_m_heightmap_100m_chunk.length; i++) {
            for (int j = 0; j < nearsight_m_heightmap_100m_chunk[0].length; j++) {
                int[] corner = new int[]{player_world_m[0] - nearsight_distance_m + i * 100, player_world_m[1] - nearsight_distance_m + j * 100};
                nearsight_m_heightmap_100m_chunk[i][j] = generate_100x100_nearsight_using_farsight_with_corner(corner);
            }
        }
    }
}
