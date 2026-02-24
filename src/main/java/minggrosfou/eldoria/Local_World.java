package minggrosfou.eldoria;

public class Local_World {
    private static final int nearsight_distance_m = 50; //50m
    private static final int farsight_distance_m = 5000; //5km
    private static final int farsight_distance_km = farsight_distance_m / 1000;

    public int[][] farsight_heightmap_m;
    public int[][] nearsight_heightmap_m;

    private final World world;

    private int[] current_center_m = new int[]{-1, -1};
    private int[] current_center_km = new int[]{-1, -1};

    private static int[] center_m(int[] center_cm) {
        return new int[]{center_cm[0] / 100, center_cm[1] / 100};
    }

    private static int[] center_km(int[] center_cm) {
        return new int[]{center_cm[0] / 1000000, center_cm[1] / 1000000};
    }

    private static int[] farsight_bottom_left_m(int[] center_cm) {
        int[] center_m = Local_World.center_m(center_cm);
        return new int[]{center_m[0] - farsight_distance_m, center_m[1] - farsight_distance_m};
    }

    private static int[] nearsight_bottom_left_m(int[] center_cm) {
        int[] center_m = Local_World.center_m(center_cm);
        return new int[]{center_m[0] - nearsight_distance_m, center_m[1] - nearsight_distance_m};
    }

    public Local_World(World world, Player player) {
        this.world = world;

        //generate_if_needed(player);
    }

    public void generate_if_needed(Player player) {
        int[] center_cm = new int[]{(int) player.world_x_cm, (int) player.world_y_cm};
        int[] center_m = Local_World.center_m(center_cm);
        int[] center_km = Local_World.center_km(center_cm);

        if (Custom_Math.distance(center_m[0], center_m[1], current_center_m[0], current_center_m[1]) > 10) {
            generate_nearsight_heightmap_m(center_cm);
            current_center_m = center_m;
        }
        if (Custom_Math.distance(center_km[0], center_km[1], current_center_km[0], current_center_km[1]) > 2) {
            generate_farsight_heightmap_m(center_cm);
            current_center_km = center_km;
        }

        System.out.println("Generated local world");
    }

    private void assign_rough_heights(World world, int[] center_km) {
        for (int i = 0; i < farsight_heightmap_m.length; i++) {
            for (int j = 0; j < farsight_heightmap_m[0].length; j++) {
                int world_x_km = center_km[0] - farsight_distance_km + i / 1000;
                int world_y_km = center_km[1] - farsight_distance_km + j / 1000;
                farsight_heightmap_m[i][j] = world.heightmap_km[world_x_km][world_y_km];
            }
        }
    }

    private void generate_farsight_heightmap_m(int[] center_cm) {
        int[] center_km = Local_World.center_km(center_cm);
        int[] farsight_bottom_left_m = Local_World.farsight_bottom_left_m(center_cm);

        //heightmap that includes the m-precision version of the surrounding 10^2 grid cells at km-precision
        farsight_heightmap_m = new int[2 * farsight_distance_m][2 * farsight_distance_m];
        assign_rough_heights(world, center_km);

        //smooth, because there may be seams between 10km perlin cells
        for (int i = 0; i < 50; i++) {
            Custom_Math.average_smooth_heightmap(farsight_heightmap_m);
        }

        //there's already Perlin noise for 10km precision, now we do: 5km, 1km
        for (int i = 0; i < farsight_heightmap_m.length; i++) {
            for (int j = 0; j < farsight_heightmap_m[0].length; j++) {
                farsight_heightmap_m[i][j] += Custom_Math.perlin_at(farsight_bottom_left_m[0] + i, farsight_bottom_left_m[1] + j, 5000, 200);
                farsight_heightmap_m[i][j] += Custom_Math.perlin_at(farsight_bottom_left_m[0] + i, farsight_bottom_left_m[1] + j, 1000, 50);
            }
        }
    }

    private void sample_nearsight_from_farsight() {
        for (int i = 0; i < nearsight_distance_m * 2; i++) {
            for (int j = 0; j < nearsight_distance_m * 2; j++) {
                nearsight_heightmap_m[i][j] = farsight_heightmap_m[farsight_distance_m - nearsight_distance_m + i][farsight_distance_m - nearsight_distance_m + j];
            }
        }
    }

    private void generate_nearsight_heightmap_m(int[] center_cm) {
        int[] nearsight_bottom_left_m = Local_World.nearsight_bottom_left_m(center_cm);

        nearsight_heightmap_m = new int[2 * nearsight_distance_m][2 * nearsight_distance_m];
        sample_nearsight_from_farsight();

        //100m, 10m, 1m precision perlin noise
        for (int i = 0; i < nearsight_heightmap_m.length; i++) {
            for (int j = 0; j < nearsight_heightmap_m[0].length; j++) {
                nearsight_heightmap_m[i][j] += Custom_Math.perlin_at(nearsight_bottom_left_m[0] + i, nearsight_bottom_left_m[1] + j, 100, 5);
                nearsight_heightmap_m[i][j] += Custom_Math.perlin_at(nearsight_bottom_left_m[0] + i, nearsight_bottom_left_m[1] + j, 10, 1);
                nearsight_heightmap_m[i][j] += Custom_Math.perlin_at(nearsight_bottom_left_m[0] + i, nearsight_bottom_left_m[1] + j, 1, 0.2);
            }
        }
    }
}
