package minggrosfou.eldoria;

import java.util.Random;

public class Custom_Math {
    public static int factorial(int n) {
        int prod = 1;
        for (int i = 2; i <= n; i++) {
            prod *= i;
        }
        return prod;
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2d) + Math.pow(y1 - y2, 2d));
    }

    public static int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    //custom shape for mountain
    public static double mountain_increment(int max_height, int mountain_width, int x) {
        return (max_height * Math.pow(1.1d, -x) / (mountain_width * mountain_width * 4));
    }

    //custom shape for lakes
    public static double lake_increment(int lake_depth, int lake_width, double distance) {
        return -lake_depth * Math.exp(-(Math.pow(distance / lake_width * 2, 2)));
    }

    //Perlin fade
    public static double fade(double t) {
        return (6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3));
    }

    //Perlin lerp
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    //we want the noise to be pseudo-random, deterministic
    //in other words, when the player comes back to the location, the same landscape should exist
    //but the landscape will still feel organic
    //I'm usually assigning SEED = WORLD_X * WORLD_WIDTH + WORLD_Y
    //please select stride to be a divisor of both heightmap dimensions
    //amplitude in same unit as heightmap
    public static int perlin_at(int x, int y, int stride, double amplitude) {
        Random random1 = new Random(1234567890L * (x / stride) + 123L * (y / stride));
        Random random2 = new Random(1234567890L * ((x + 1) / stride) + 123L * (y / stride));
        Random random3 = new Random(1234567890L * ((x + 1) / stride) + 123L * ((y + 1) / stride));
        Random random4 = new Random(1234567890L * (x / stride) + 123L * ((y + 1) / stride));

        double dir1 = random1.nextDouble() * Math.PI * 2;
        double dir2 = random2.nextDouble() * Math.PI * 2;
        double dir3 = random3.nextDouble() * Math.PI * 2;
        double dir4 = random4.nextDouble() * Math.PI * 2;

        double[] vector1 = new double[]{Math.cos(dir1), Math.sin(dir1)};
        double[] vector2 = new double[]{Math.cos(dir2), Math.sin(dir2)};
        double[] vector3 = new double[]{Math.cos(dir3), Math.sin(dir3)};
        double[] vector4 = new double[]{Math.cos(dir4), Math.sin(dir4)};

        int local_x = x % stride;
        int local_y = y % stride;

        double u = fade((double) local_x / stride);
        double v = fade((double) local_y / stride);

        double[] offset1 = new double[]{local_x, local_y};
        double[] offset2 = new double[]{local_x - stride, local_y};
        double[] offset3 = new double[]{local_x - stride, local_y - stride};
        double[] offset4 = new double[]{local_x, local_y - stride};

        double dot1 = offset1[0] * vector1[0] + offset1[1] * vector1[1];
        double dot2 = offset2[0] * vector2[0] + offset2[1] * vector2[1];
        double dot3 = offset3[0] * vector3[0] + offset3[1] * vector3[1];
        double dot4 = offset4[0] * vector4[0] + offset4[1] * vector4[1];

        double ix0 = Custom_Math.lerp(dot1, dot2, u);
        double ix1 = Custom_Math.lerp(dot4, dot3, u);
        double noise = Custom_Math.lerp(ix0, ix1, v);

        return (int) (amplitude * noise);
    }

    //this Perlin is truly random, it will change even if everything was the same
    public static void add_perlin(int[][] heightmap, int stride, int start_x, int start_y, int end_x, int end_y, double amplitude, long seed) {
        int grid_width = (end_x - start_x) / stride;
        int grid_height = (end_y - start_y) / stride;

        Random random = new Random(seed);

        double[][][] vectors = new double[grid_width + 1][grid_height + 1][2];
        for (int i = 0; i < grid_width + 1; i++) {
            for (int j = 0; j < grid_height + 1; j++) {
                double dir = random.nextDouble() * Math.PI * 2;
                vectors[i][j][0] = Math.cos(dir);
                vectors[i][j][1] = Math.sin(dir);
            }
        }

        for (int x = start_x; x < end_x; x++) {
            for (int y = start_y; y < end_y; y++) {
                int grid_x = (x - start_x) / stride;
                int grid_y = (y - start_y) / stride;

                double local_x = (double) ((x - start_x) % stride) / stride;
                double local_y = (double) ((y - start_y) % stride) / stride;

                double u = fade(local_x / stride);
                double v = fade(local_y / stride);

                double[] offset1 = new double[]{local_x, local_y};
                double[] offset2 = new double[]{local_x - stride, local_y};
                double[] offset3 = new double[]{local_x - stride, local_y - stride};
                double[] offset4 = new double[]{local_x, local_y - stride};

                double dot1 = offset1[0] * vectors[grid_x][grid_y][0] + offset1[1] * vectors[grid_x][grid_y][1];
                double dot2 = offset2[0] * vectors[grid_x + 1][grid_y][0] + offset2[1] * vectors[grid_x + 1][grid_y][1];
                double dot3 = offset3[0] * vectors[grid_x + 1][grid_y + 1][0] + offset3[1] * vectors[grid_x + 1][grid_y + 1][1];
                double dot4 = offset4[0] * vectors[grid_x][grid_y + 1][0] + offset4[1] * vectors[grid_x][grid_y + 1][1];

                double ix0 = Custom_Math.lerp(dot1, dot2, u);
                double ix1 = Custom_Math.lerp(dot4, dot3, u);
                double noise = Custom_Math.lerp(ix0, ix1, v);

                heightmap[x][y] += (int) (amplitude * noise);
            }
        }
    }

    //this smoothing always preserves borders, which is desired when we concatenate grid cells
    //however, this may create grid artifacts
    public static void average_smooth_heightmap(int[][] heightmap) {
        for (int y = 1; y < heightmap[0].length - 1; y++) {
            for (int x = 1; x < heightmap.length - 1; x++) {
                smooth_coord(x, y, heightmap);
            }
        }
        for (int x = 1; x < heightmap.length - 1; x++) {
            for (int y = 1; y < heightmap[0].length - 1; y++) {
                smooth_coord(x, y, heightmap);
            }
        }
    }

    static void smooth_coord(int x, int y, int[][] heightmap) {
        int average = 0;
        for (int local_x = x - 1; local_x <= x + 1; local_x++) {
            for (int local_y = y - 1; local_y <= y + 1; local_y++) {
                average += heightmap[local_x][local_y];
            }
        }
        average /= 9;
        heightmap[x][y] = average;
    }
}
