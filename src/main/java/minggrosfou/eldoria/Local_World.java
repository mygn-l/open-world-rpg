package minggrosfou.eldoria;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Local_World {
    private static final int nearsight_distance_m = 50; //50m
    private static final int farsight_distance_m = 3000; //5km
    private static final int farsight_distance_km = farsight_distance_m / 1000;

    public int[][] farsight_heightmap_10m;
    public double[][] nearsight_heightmap_m;

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

    public static int[] nearsight_bottom_left_m(int[] center_cm) {
        int[] center_m = Local_World.center_m(center_cm);
        return new int[]{center_m[0] - nearsight_distance_m, center_m[1] - nearsight_distance_m};
    }

    public Local_World(World world, Player player) {
        this.world = world;

        generate_if_needed(player);

        /*
        Local_World self = this;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable actionToBePerformed = () -> player.fall(self);
        executor.scheduleAtFixedRate(actionToBePerformed, 0, 100, TimeUnit.MILLISECONDS);
        */

        int[] center_cm = new int[]{(int) player.world_x_cm, (int) player.world_y_cm};
        int[] center_m = Local_World.center_m(center_cm);
        int[] center_km = Local_World.center_km(center_cm);
        generate_farsight_heightmap_m(center_cm);
        current_center_km = center_km;
        generate_nearsight_heightmap_m(center_cm);
        current_center_m = center_m;
    }

    public void generate_if_needed(Player player) {
        int[] center_cm = new int[]{(int) player.world_x_cm, (int) player.world_y_cm};
        int[] center_m = Local_World.center_m(center_cm);
        int[] center_km = Local_World.center_km(center_cm);

        if (Custom_Math.distance(current_center_km[0], current_center_km[1], center_km[0], center_km[1]) > 1) {
            generate_farsight_heightmap_m(center_cm);
            current_center_km = center_km;
        }

        if (Custom_Math.distance(current_center_m[0], current_center_m[1], center_m[0], center_m[1]) > 10) {
            generate_nearsight_heightmap_m(center_cm);
            current_center_m = center_m;
        }

        System.out.println("Generated local world");
    }

    private void assign_rough_heights(World world, int[] center_km) {
        for (int i = 0; i < farsight_heightmap_10m.length; i++) {
            for (int j = 0; j < farsight_heightmap_10m[0].length; j++) {
                int world_x_km = center_km[0] - farsight_distance_km + i / 100;
                int world_y_km = center_km[1] - farsight_distance_km + j / 100;
                farsight_heightmap_10m[i][j] = world.heightmap_km[world_x_km][world_y_km];
            }
        }
    }

    private void generate_farsight_heightmap_m(int[] center_cm) {
        int[] center_km = Local_World.center_km(center_cm);
        int[] farsight_bottom_left_m = Local_World.farsight_bottom_left_m(center_cm);

        //heightmap that includes the m-precision version of the surrounding 10^2 grid cells at km-precision
        farsight_heightmap_10m = new int[2 * farsight_distance_m / 10][2 * farsight_distance_m / 10];
        assign_rough_heights(world, center_km);
        System.out.println("Assigned rough heightmap to farsight");

        //smooth, because there may be seams between 10km perlin cells
        for (int i = 0; i < 5; i++) {
            Custom_Math.average_smooth_heightmap(farsight_heightmap_10m);
        }
        System.out.println("Smoothed");

        Custom_Math.add_perlin(farsight_heightmap_10m, 300, 0, 0, farsight_heightmap_10m.length, farsight_heightmap_10m[0].length, farsight_bottom_left_m[0], farsight_bottom_left_m[1], 200);
        Custom_Math.add_perlin(farsight_heightmap_10m, 100, 0, 0, farsight_heightmap_10m.length, farsight_heightmap_10m[0].length, farsight_bottom_left_m[0], farsight_bottom_left_m[1], 50);
        System.out.println("Farsight Perlin done");
    }

    private void sample_nearsight_from_farsight() {
        for (int i = 0; i < nearsight_distance_m * 2; i++) {
            for (int j = 0; j < nearsight_distance_m * 2; j++) {
                nearsight_heightmap_m[i][j] = farsight_heightmap_10m[(farsight_distance_m - nearsight_distance_m + i) / 10][(farsight_distance_m - nearsight_distance_m + j) / 10];
            }
        }
    }

    private void generate_nearsight_heightmap_m(int[] center_cm) {
        int[] nearsight_bottom_left_m = Local_World.nearsight_bottom_left_m(center_cm);

        nearsight_heightmap_m = new double[2 * nearsight_distance_m][2 * nearsight_distance_m];
        sample_nearsight_from_farsight();
        System.out.println("Sampled nearsight from farsight");

        for (int i = 0; i < 5; i++) {
            Custom_Math.average_smooth_heightmap(nearsight_heightmap_m);
        }
        System.out.println("Smoothed");

        Custom_Math.add_perlin(nearsight_heightmap_m, 100, 0, 0, nearsight_heightmap_m.length, nearsight_heightmap_m[0].length, nearsight_bottom_left_m[0], nearsight_bottom_left_m[1], 5);
        Custom_Math.add_perlin(nearsight_heightmap_m, 10, 0, 0, nearsight_heightmap_m.length, nearsight_heightmap_m[0].length, nearsight_bottom_left_m[0], nearsight_bottom_left_m[1], 1);
        Custom_Math.add_perlin(nearsight_heightmap_m, 5, 0, 0, nearsight_heightmap_m.length, nearsight_heightmap_m[0].length, nearsight_bottom_left_m[0], nearsight_bottom_left_m[1], 0.5);
        System.out.println("Nearsight Perlin done");
    }
}
