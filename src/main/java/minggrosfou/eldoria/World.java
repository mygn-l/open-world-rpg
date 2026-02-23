package minggrosfou.eldoria;

import java.util.ArrayDeque;
import java.util.Queue;

public class World {
    //min x = 0, min y = 0 implicitly in this code
    //distances in km, height in meter
    public static final int max_x_km = 5200; //kilometers, west = 0, east = 5000
    public static final int max_y_km = 5500; //kilometers, north = 5000, south = 0
    public static final int mountain_width_km = 100;
    public static final int mountain_max_height_m = 20000;
    public static final int ocean_depth_m = -5000;
    public static final int continental_height_m = 1000;
    public static final int num_plates = 20;
    public static final int num_volcanoes = 2;
    public static final int num_lakes = 2;
    public static final int lake_max_width_km = 500;
    public static final int lake_depth_m = 1000;

    public static final int num_desert_seeds = 3;
    public static final int num_tropical_seeds = 5;
    public static final int num_forest_seeds = 10;
    public static final int num_grassland_seeds = 10;
    public static final int num_savanna_seeds = 2;
    public static final int num_river_seeds = 10;
    public static final int num_wetland_seeds = 2;
    Queue<Integer> frontier_x = new ArrayDeque<>();
    Queue<Integer> frontier_y = new ArrayDeque<>();
    Queue<Biome> frontier_biome = new ArrayDeque<>();

    int[][] tectonic_centers;
    int[][] belongings;
    int[] tectonic_elevation;
    double[][] rough_tectonic_heightmap; //10km precision flat, meter precision vertical

    public int[][] km_heightmap; //km precision

    public enum Biome {
        NIVAL(0),
        ALPINE(1),
        REEF(2),
        OCEAN(3),
        LAKE(4),
        POLAR(5),
        COASTAL(6),
        TUNDRA(7),
        TEMPERATE_FOREST(8),
        TROPICAL(9),
        DESERT(10),
        TEMPERATE_GRASSLAND(11),
        SAVANNA(12),
        RIVER(13),
        WETLAND(14);

        private final int id;

        Biome(int id) {
            this.id = id;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public int getId() {
            return id;
        }
    }

    public Biome[][] biomes = new Biome[max_x_km][max_y_km];

    void assign_mountainous_zone() {
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                if (km_heightmap[x][y] > 4000) {
                    biomes[x][y] = Biome.NIVAL;
                } else if (km_heightmap[x][y] > 2000) {
                    biomes[x][y] = Biome.ALPINE;
                }
            }
        }
    }

    void assign_aquatic_zone() {
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                if (tectonic_elevation[belongings[x / 10][y / 10]] < 0) {
                    if (km_heightmap[x][y] < -200) {
                        biomes[x][y] = Biome.OCEAN;
                    } else if (km_heightmap[x][y] < 0) {
                        biomes[x][y] = Biome.REEF;
                    }
                } else {
                    if (km_heightmap[x][y] < 0) {
                        biomes[x][y] = Biome.LAKE;
                    }
                }
            }
        }
    }

    void assign_polar_zone() {
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km / 6; y++) {
                if (biomes[x][y] == null) {
                    biomes[x][y] = Biome.POLAR;
                }
            }
            for (int y = max_y_km / 6; y < max_y_km / 5; y++) {
                if (biomes[x][y] == null) {
                    biomes[x][y] = Biome.TUNDRA;
                }
            }

            for (int y = (int) (5d / 6d * max_y_km); y < max_y_km; y++) {
                if (biomes[x][y] == null) {
                    biomes[x][y] = Biome.POLAR;
                }
            }
            for (int y = (int) (4d / 5d * max_y_km); y < (int) (5d / 6d * max_y_km); y++) {
                if (biomes[x][y] == null) {
                    biomes[x][y] = Biome.TUNDRA;
                }
            }
        }
    }

    void assign_coastal_zone() {
        for (int x = 50; x < max_x_km - 50; x++) {
            for (int y = 50; y < max_y_km - 50; y++) {
                int water_count = 0;
                int land_count = 0;
                for (int i = x - 5; i < x + 5; i++) {
                    for (int j = y - 5; j < y + 5; j++) {
                        if (km_heightmap[i][j] < 0) {
                            water_count++;
                        } else {
                            land_count++;
                        }
                    }
                }
                if (Math.abs(water_count - land_count) < 6) {
                    biomes[x][y] = Biome.COASTAL;
                }
            }
        }
    }

    void seed_desert() {
        for (int i = 0; i < num_desert_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.4 + 0.2) * max_y_km);

                if (biomes[location_x][location_y] == null) {
                    int min_dist = 500;
                    for (int x = Math.max(0, location_x - 500); x < Math.min(max_x_km, location_x + 500); x++) {
                        for (int y = Math.max(0, location_y - 500); y < Math.min(max_y_km, location_y + 500); y++) {
                            if (km_heightmap[x][y] < 0) {
                                int dist = Custom_Math.distance(x, y, location_x, location_y);
                                if (dist < min_dist) {
                                    min_dist = dist;
                                }
                            }
                        }
                    }
                    if (Math.random() < Math.pow((double) min_dist / 500, 2)) {
                        found_good_place = true;
                        biomes[location_x][location_y] = Biome.DESERT;
                        frontier_x.add(location_x);
                        frontier_y.add(location_y);
                        frontier_biome.add(Biome.DESERT);
                    }
                }
            }
        }
        for (int i = 0; i < num_savanna_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.4 + 0.2) * max_y_km);

                if (biomes[location_x][location_y] == null) {
                    int min_dist = 500;
                    for (int x = Math.max(0, location_x - 500); x < Math.min(max_x_km, location_x + 500); x++) {
                        for (int y = Math.max(0, location_y - 500); y < Math.min(max_y_km, location_y + 500); y++) {
                            if (biomes[x][y] == Biome.DESERT) {
                                int dist = Custom_Math.distance(x, y, location_x, location_y);
                                if (dist < min_dist) {
                                    min_dist = dist;
                                }
                            }
                        }
                    }
                    if (Math.random() < Math.pow((double) min_dist / 500, 2)) {
                        found_good_place = true;
                        biomes[location_x][location_y] = Biome.SAVANNA;
                        frontier_x.add(location_x);
                        frontier_y.add(location_y);
                        frontier_biome.add(Biome.SAVANNA);
                    }
                }
            }
        }
    }

    void seed_tropical() {
        for (int i = 0; i < num_tropical_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.35 + 0.3) * max_y_km);

                if (biomes[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes[location_x][location_y] = Biome.TROPICAL;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.TROPICAL);
                }
            }
        }
    }

    void seed_forest_and_grassland() {
        for (int i = 0; i < num_forest_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y;
                if (Math.random() < 0.5) {
                    location_y = (int) ((Math.random() * 0.2 + 0.2) * max_y_km);
                } else {
                    location_y = (int) ((Math.random() * 0.6 + 0.2) * max_y_km);
                }

                if (biomes[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes[location_x][location_y] = Biome.TEMPERATE_FOREST;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.TEMPERATE_FOREST);
                }
            }
        }
        for (int i = 0; i < num_grassland_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y;
                if (Math.random() < 0.5) {
                    location_y = (int) ((Math.random() * 0.2 + 0.2) * max_y_km);
                } else {
                    location_y = (int) ((Math.random() * 0.6 + 0.2) * max_y_km);
                }

                if (biomes[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes[location_x][location_y] = Biome.TEMPERATE_GRASSLAND;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.TEMPERATE_GRASSLAND);
                }
            }
        }
    }

    void seed_wetland_and_river() {
        for (int i = 0; i < num_wetland_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.2 + 0.6) * max_y_km);

                if (biomes[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes[location_x][location_y] = Biome.WETLAND;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.WETLAND);
                }
            }
        }
        for (int i = 0; i < num_river_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.2 + 0.6) * max_y_km);

                if (biomes[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes[location_x][location_y] = Biome.RIVER;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.RIVER);
                }
            }
        }
    }

    void copy_biome(int x, int y, Biome biome) {
        if (biomes[x][y] == null) {
            biomes[x][y] = biome;
            frontier_x.add(x);
            frontier_y.add(y);
            frontier_biome.add(biome);
        }
    }

    void expand_seeds() {
        while (!frontier_x.isEmpty()) {
            int x = frontier_x.poll();
            int y = frontier_y.poll();
            Biome biome = frontier_biome.poll();

            if (!(x > 0 && x < max_x_km - 1 && y > 0 && y < max_y_km)) {
                continue;
            }

            if (biome == Biome.RIVER) {
                if (biomes[x - 1][y] == Biome.RIVER) {
                    if (Math.random() > 0.2) {
                        copy_biome(x + 1, y, biome);
                    } else {
                        if (Math.random() < 0.5) {
                            copy_biome(x, y + 1, biome);
                        } else {
                            copy_biome(x, y - 1, biome);
                        }
                    }
                } else if (biomes[x + 1][y] == Biome.RIVER) {
                    if (Math.random() > 0.2) {
                        copy_biome(x - 1, y, biome);
                    } else {
                        if (Math.random() < 0.5) {
                            copy_biome(x, y + 1, biome);
                        } else {
                            copy_biome(x, y - 1, biome);
                        }
                    }
                } else if (biomes[x][y - 1] == null) {
                    if (Math.random() > 0.2) {
                        copy_biome(x, y + 1, biome);
                    } else {
                        if (Math.random() < 0.5) {
                            copy_biome(x - 1, y, biome);
                        } else {
                            copy_biome(x + 1, y, biome);
                        }
                    }
                } else if (biomes[x][y + 1] == null) {
                    if (Math.random() > 0.2) {
                        copy_biome(x, y - 1, biome);
                    } else {
                        if (Math.random() < 0.5) {
                            copy_biome(x - 1, y, biome);
                        } else {
                            copy_biome(x + 1, y, biome);
                        }
                    }
                }
            } else if (biome == Biome.WETLAND) {
                if (Math.random() > 0.1) {
                    copy_biome(x - 1, y, biome);
                }
                if (Math.random() > 0.1) {
                    copy_biome(x + 1, y, biome);
                }
                if (Math.random() > 0.1) {
                    copy_biome(x, y - 1, biome);
                }
                if (Math.random() > 0.1) {
                    copy_biome(x, y + 1, biome);
                }
            } else {
                copy_biome(x - 1, y, biome);
                copy_biome(x + 1, y, biome);
                copy_biome(x, y - 1, biome);
                copy_biome(x, y + 1, biome);
            }
        }
    }

    void generate_rough_tectonic_heightmap() {
        tectonic_centers = new int[num_plates][2];
        for (int i = 0; i < num_plates; i++) {
            tectonic_centers[i][0] = (int) (Math.random() * max_x_km);
            tectonic_centers[i][1] = (int) (Math.random() * max_y_km);
        }

        tectonic_elevation = new int[num_plates];
        for (int i = 0; i < num_plates; i++) {
            if (Math.random() > 0.2) {
                tectonic_elevation[i] = continental_height_m;
            } else {
                tectonic_elevation[i] = ocean_depth_m;
            }
        }

        belongings = new int[max_x_km / 10][max_y_km / 10];
        for (int x = 0; x < max_x_km / 10; x++) {
            for (int y = 0; y < max_y_km / 10; y++) {
                double min_dist = max_x_km + max_y_km; //safe initial qty
                int associated_index = -1;
                for (int i = 0; i < num_plates; i++) {
                    double dist = Custom_Math.distance(x * 10, y * 10, tectonic_centers[i][0], tectonic_centers[i][1]);
                    if (dist < min_dist) {
                        min_dist = dist;
                        associated_index = i;
                    }
                }
                belongings[x][y] = associated_index;

                if (x == 0 || y == 0 || x == max_x_km / 10 - 1 || y == max_y_km / 10 - 1) {
                    tectonic_elevation[associated_index] = ocean_depth_m;
                }
            }
        }

        rough_tectonic_heightmap = new double[max_x_km / 10][max_y_km / 10];
        for (int x = 0; x < max_x_km / 10; x++) {
            for (int y = 0; y < max_y_km / 10; y++) {
                rough_tectonic_heightmap[x][y] = tectonic_elevation[belongings[x][y]];
            }
        }
    }

    void smooth_coord_if_salient(int x, int y, int[][] heightmap) {
        boolean is_salient = !(
                heightmap[x - 1][y] == heightmap[x + 1][y] &&
                        heightmap[x - 1][y - 1] == heightmap[x + 1][y + 1] &&
                        heightmap[x][y - 1] == heightmap[x][y + 1] &&
                        heightmap[x + 1][y - 1] == heightmap[x - 1][y + 1]
        );

        if (is_salient) {
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

    void smooth_coord_if_salient(int x, int y, double[][] heightmap) {
        boolean is_salient = !(
                heightmap[x - 1][y] == heightmap[x + 1][y] &&
                        heightmap[x - 1][y - 1] == heightmap[x + 1][y + 1] &&
                        heightmap[x][y - 1] == heightmap[x][y + 1] &&
                        heightmap[x + 1][y - 1] == heightmap[x - 1][y + 1]
        );

        if (is_salient) {
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

    void smooth_salient_heightmap(int[][] heightmap, boolean exchange_order) {
        if (exchange_order) {
            for (int y = 1; y < heightmap[0].length - 1; y++) {
                for (int x = 1; x < heightmap.length - 1; x++) {
                    smooth_coord_if_salient(x, y, heightmap);
                }
            }
        } else {
            for (int x = 1; x < heightmap.length - 1; x++) {
                for (int y = 1; y < heightmap[0].length - 1; y++) {
                    smooth_coord_if_salient(x, y, heightmap);
                }
            }
        }
    }

    void smooth_salient_heightmap(double[][] heightmap, boolean exchange_order) {
        if (exchange_order) {
            for (int y = 1; y < heightmap[0].length - 1; y++) {
                for (int x = 1; x < heightmap.length - 1; x++) {
                    smooth_coord_if_salient(x, y, heightmap);
                }
            }
        } else {
            for (int x = 1; x < heightmap.length - 1; x++) {
                for (int y = 1; y < heightmap[0].length - 1; y++) {
                    smooth_coord_if_salient(x, y, heightmap);
                }
            }
        }
    }

    void generate_mountains_and_rifts_from_rough() {
        double[][] tectonic_movement = new double[num_plates][2];
        for (int i = 0; i < num_plates; i++) {
            double mag = Math.random();
            double dir = Math.random() * Math.PI * 2;
            tectonic_movement[i][0] = mag * Math.cos(dir);
            tectonic_movement[i][1] = mag * Math.sin(dir);
        }

        for (int x = mountain_width_km / 10; x < (max_x_km - mountain_width_km) / 10; x++) {
            for (int y = mountain_width_km / 10; y < (max_y_km - mountain_width_km) / 10; y++) {
                boolean is_tectonic_boundary = !(
                        belongings[x - 1][y] == belongings[x + 1][y] &&
                                belongings[x - 1][y - 1] == belongings[x + 1][y + 1] &&
                                belongings[x][y - 1] == belongings[x][y + 1] &&
                                belongings[x + 1][y - 1] == belongings[x - 1][y + 1]
                );

                if (is_tectonic_boundary) {
                    int belonging1 = belongings[x][y];
                    int belonging2 = -1;
                    for (int i = x - 1; i <= x + 1; i++) {
                        for (int j = y - 1; j <= y + 1; j++) {
                            if (belongings[i][j] != belonging1) {
                                belonging2 = belongings[i][j];
                                break;
                            }
                        }
                    }

                    double[] center_to_center = new double[]{
                            tectonic_centers[belonging2][0] - tectonic_centers[belonging1][0],
                            tectonic_centers[belonging2][1] - tectonic_centers[belonging1][1]
                    };
                    double len = Custom_Math.distance(0, 0, center_to_center[0], center_to_center[1]);
                    center_to_center[0] /= len;
                    center_to_center[1] /= len;
                    double[] relative_motion = new double[]{
                            tectonic_movement[belonging2][0] - tectonic_movement[belonging1][0],
                            tectonic_movement[belonging2][1] - tectonic_movement[belonging1][1]
                    };
                    double dot = center_to_center[0] * relative_motion[0] + center_to_center[1] * relative_motion[1];
                    //dot < 0 is convergent
                    for (int x_local = x - mountain_width_km / 10; x_local <= x + mountain_width_km / 10; x_local++) {
                        for (int y_local = y - mountain_width_km / 10; y_local <= y + mountain_width_km / 10; y_local++) {
                            int distance = Custom_Math.distance(x, y, x_local, y_local);
                            rough_tectonic_heightmap[x_local][y_local] += (-dot) * Custom_Math.mountain_increment(mountain_max_height_m, mountain_width_km / 10, distance);
                        }
                    }
                }
            }
        }
    }

    void convert_rough_to_km() {
        km_heightmap = new int[max_x_km][max_y_km];
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                km_heightmap[x][y] = (int) rough_tectonic_heightmap[x / 10][y / 10];
            }
        }
    }

    void add_perlin_km_heightmap(int stride) {
        int grid_width = km_heightmap.length / stride;
        int grid_height = km_heightmap[0].length / stride;

        double[][] hill_strength = new double[grid_width][grid_height];
        double[][][] vectors = new double[grid_width + 1][grid_height + 1][2];
        for (int i = 0; i < grid_width + 1; i++) {
            for (int j = 0; j < grid_height + 1; j++) {
                double dir = Math.random() * Math.PI * 2;
                vectors[i][j][0] = Math.cos(dir);
                vectors[i][j][1] = Math.sin(dir);

                if (i < grid_width && j < grid_height) {
                    hill_strength[i][j] = Math.pow(Math.random(), 3); //favor smaller hills with x^3
                }
            }
        }

        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                int grid_x = x / stride;
                int grid_y = y / stride;

                int local_x = x % stride;
                int local_y = y % stride;

                double u = Custom_Math.fade((double) local_x / stride);
                double v = Custom_Math.fade((double) local_y / stride);

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

                km_heightmap[x][y] += (int) ((double) stride / 5 * noise * hill_strength[grid_x][grid_y]);
            }
        }
    }

    void spawn_volcanoes() {
        for (int i = 0; i < num_volcanoes; i++) {
            int location_x = (int) ((Math.random() * 0.5 + 0.25) * max_x_km);
            int location_y = (int) ((Math.random() * 0.5 + 0.25) * max_y_km);
            int height = (int) ((2 + Math.random()) * mountain_max_height_m);

            for (int x = location_x - mountain_width_km; x < location_x + mountain_width_km; x++) {
                for (int y = location_y - mountain_width_km; y < location_y + mountain_width_km; y++) {
                    int distance = Custom_Math.distance(location_x, location_y, x, y);
                    km_heightmap[x][y] += (int) Math.ceil(Custom_Math.mountain_increment(height, 1, distance));
                }
            }
        }
    }

    void spawn_lakes() {
        for (int i = 0; i < num_lakes; i++) {
            int location_x = (int) ((Math.random() * 0.5 + 0.25) * max_x_km);
            int location_y = (int) ((Math.random() * 0.5 + 0.25) * max_y_km);
            int width = (int) (Math.random() * lake_max_width_km);

            for (int x = location_x - width; x < location_x + width; x++) {
                for (int y = location_y - width; y < location_y + width; y++) {
                    int distance = Custom_Math.distance(location_x, location_y, x, y);
                    km_heightmap[x][y] += (int) Math.ceil(Custom_Math.lake_increment(lake_depth_m, width, distance));
                }
            }
        }
    }

    public World() {
        this.generate_rough_tectonic_heightmap();
        for (int i = 0; i < 50; i++) {
            this.smooth_salient_heightmap(rough_tectonic_heightmap, i % 2 == 0);
        }
        System.out.println("Tectonic done");

        this.generate_mountains_and_rifts_from_rough();
        System.out.println("Mountains done");

        this.convert_rough_to_km();
        System.out.println("Converted to per km");

        this.add_perlin_km_heightmap(100);
        for (int i = 0; i < 10; i++) {
            this.smooth_salient_heightmap(km_heightmap, i % 2 == 0);
        }
        System.out.println("Perlin 100 done");

        this.add_perlin_km_heightmap(50);
        for (int i = 0; i < 4; i++) {
            this.smooth_salient_heightmap(km_heightmap, i % 2 == 0);
        }
        System.out.println("Perlin 50 done");

        this.spawn_lakes();
        System.out.println("Lakes done");

        this.spawn_volcanoes();
        System.out.println("Volcanoes done");

        this.add_perlin_km_heightmap(10);
        System.out.println("Perlin 10 done");

        this.assign_aquatic_zone();
        this.assign_polar_zone();
        this.assign_coastal_zone();
        this.assign_mountainous_zone();
        System.out.println("Aquatic, polar, coastal, and mountainous biomes assigned");

        this.seed_desert();
        this.seed_tropical();
        this.seed_forest_and_grassland();
        this.seed_wetland_and_river();
        System.out.println("Desert, savanna, tropical, forest, grassland, wetland, river seeded");

        this.expand_seeds();
        System.out.println("Expanded desert, tropical, forest seeds");

        System.out.println("Finished generating world");
    }
}
