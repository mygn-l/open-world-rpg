package minggrosfou.eldoria;

public class Custom_Math {
    public static int factorial(int n) {
        int prod = 1;
        for (int i = 2; i <= n; i++) {
            prod *= i;
        }
        return prod;
    }

    public static int seed = 0;

    public static int get_seed() {
        return seed++;
    }

    public static void set_seed(int seed) {
        Custom_Math.seed = seed;
    }

    public static double pseudo_random() {
        long lol = get_seed() * 1234567890L;
        lol ^= (lol >>> 33);
        lol *= 0xff51afd7ed558ccdL;
        lol ^= (lol >>> 33);
        lol *= 0xc4ceb9fe1a85ec53L;
        lol ^= (lol >>> 33);
        return (lol >>> 11) * (1.0 / (1L << 53));
    }

    public static double[] unit_random_vector_2d() {
        double magnitude = Custom_Math.pseudo_random();
        double direction = Custom_Math.pseudo_random() * Math.PI * 2;
        return new double[]{magnitude * Math.cos(direction), magnitude * Math.sin(direction)};
    }

    public static double[] random_position_2d(double min_x, double min_y, double max_x, double max_y) {
        return new double[]{
                Custom_Math.pseudo_random() * (max_x - min_x) + min_x,
                Custom_Math.pseudo_random() * (max_y - min_y) + min_y,
        };
    }

    public static int[] double_to_int_2d(double[] array) {
        return new int[]{(int) array[0], (int) array[1]};
    }

    public static double[] int_to_double_2d(int[] array) {
        return new double[]{(double) array[0], (double) array[1]};
    }

    public static double[] unit_vector_2d_from(double[] vector) {
        double length = distance(0, 0, vector[0], vector[1]);
        return new double[]{vector[0] / length, vector[1] / length};
    }

    public static double[] unit_vector_2d_from(int[] vector) {
        double[] new_vector = int_to_double_2d(vector);
        return unit_vector_2d_from(new_vector);
    }

    public static double dot_2d(double[] vector1, double[] vector2) {
        return vector1[0] * vector2[0] + vector1[1] * vector2[1];
    }

    public static double[] difference_vector_2d(double[] vector1, double[] vector2) {
        return new double[]{vector2[0] - vector1[0], vector2[1] - vector1[1]};
    }

    public static int[] difference_vector_2d(int[] vector1, int[] vector2) {
        return new int[]{vector2[0] - vector1[0], vector2[1] - vector1[1]};
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2d) + Math.pow(y1 - y2, 2d));
    }

    public static int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    //Perlin fade
    public static double fade(double t) {
        return (6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3));
    }

    //Perlin lerp
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static double perlin_constant = 4 / Math.sqrt(3);

    //sample a window from the entire perlin domain, window dimensions determined by heightmap dimensions
    //window starts at stride_shift_i * stride, stride_shift_j * stride
    //stride is the width of the perlin hills
    public static void add_perlin(int[][] heightmap, int stride, int stride_shift_i, int stride_shift_j, int max_i, double amplitude) {
        int grid_width = (heightmap.length) / stride;
        int grid_height = (heightmap[0].length) / stride;

        int total_stride_i = max_i / stride + 1;

        double[][][] vectors = new double[grid_width + 1][grid_height + 1][2];
        for (int i = 0; i < grid_width + 1; i++) {
            for (int j = 0; j < grid_height + 1; j++) {
                set_seed((i + stride_shift_i) * (total_stride_i + 1) + (j + stride_shift_j));
                vectors[i][j] = unit_random_vector_2d();
            }
        }

        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[0].length; j++) {
                int grid_x = i / stride;
                int grid_y = i / stride;

                double local_x = i % stride;
                double local_y = i % stride;

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

                heightmap[i][j] += (int) (perlin_constant / stride * amplitude * noise);
            }
        }
    }

    public static void add_perlin(double[][] heightmap, int stride, int stride_shift_i, int stride_shift_j, int max_i, double amplitude) {
        int grid_width = (heightmap.length) / stride;
        int grid_height = (heightmap[0].length) / stride;

        int total_stride_i = max_i / stride + 1;

        double[][][] vectors = new double[grid_width + 1][grid_height + 1][2];
        for (int i = 0; i < grid_width + 1; i++) {
            for (int j = 0; j < grid_height + 1; j++) {
                set_seed((i + stride_shift_i) * (total_stride_i + 1) + (j + stride_shift_j));
                vectors[i][j] = unit_random_vector_2d();
            }
        }

        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[0].length; j++) {
                int grid_x = i / stride;
                int grid_y = i / stride;

                double local_x = i % stride;
                double local_y = i % stride;

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

                heightmap[i][j] += perlin_constant / stride * amplitude * noise;
            }
        }
    }

    public static void average_smooth_heightmap(double[][] heightmap) {
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

    static void smooth_coord(int x, int y, double[][] heightmap) {
        double average = 0;
        for (int local_x = x - 1; local_x <= x + 1; local_x++) {
            for (int local_y = y - 1; local_y <= y + 1; local_y++) {
                average += heightmap[local_x][local_y];
            }
        }
        average /= 9;
        heightmap[x][y] = average;
    }
}
