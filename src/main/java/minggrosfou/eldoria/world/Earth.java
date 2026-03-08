package minggrosfou.eldoria.world;

import minggrosfou.eldoria.Custom_Math;
import minggrosfou.eldoria.entity.Entity;
import minggrosfou.eldoria.entity.Player;
import minggrosfou.eldoria.kingdom.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Earth {
    public static final int max_x_km = 5200; //west = 0, east = 5200
    public static final int max_y_km = 5500; //north = 5500, south = 0
    private static final int num_plates = 20;
    private static final int ocean_depth_m = -5000;
    private static final int ocean_biome_depth_qualify_m = -2000;
    private static final int continental_height_m = 1000;
    public static final int mountain_width_km = 100;
    private static final int mountain_max_height_m = 20000;
    private static final int num_lakes = 2;
    private static final int lake_max_width_km = 500;
    private static final int lake_depth_m = 1000;

    public final int[][] heightmap_km;

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
        RIVER(12);

        private final int id;

        Biome(int id) {
            this.id = id;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public int getId() {
            return id;
        }
    }

    private static final Biome[] biome_seeds = {
            Biome.DESERT, Biome.DESERT, Biome.DESERT,
            Biome.TROPICAL, Biome.TROPICAL, Biome.TROPICAL, Biome.TROPICAL, Biome.TROPICAL,
            Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST, Biome.TEMPERATE_FOREST,
            Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND, Biome.TEMPERATE_GRASSLAND,
            Biome.RIVER, Biome.RIVER, Biome.RIVER, Biome.RIVER, Biome.RIVER, Biome.RIVER, Biome.RIVER, Biome.RIVER, Biome.RIVER, Biome.RIVER
    };

    public final Biome[][] biomes_km;

    public enum Kingdom {
        AZUREPORT_REPUBLIC(0),
        BEASTKIN_CONFEDERACY(1),
        DESERT_STOP(2),
        DRAGON_LAIR(3),
        DWARVEN_KINGDOM(4),
        ELDORIA(5),
        GNOME_TECHNOMANCY_ENCLAVE_SPARKHAVEN(6),
        IRONWALL_HOLD(7),
        MERFOLK_KINGDOM(8),
        SILVERREACH(9),
        UNITED_HUMAN_DEMON_REALM(10),
        WORLD_TREE(11);

        private final int id;

        Kingdom(int id) {
            this.id = id;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public int getId() {
            return id;
        }
    }

    public Kingdom[][] kingdom_km = new Kingdom[max_x_km][max_y_km];

    public final Eldoria eldoria;

    public ArrayList<Entity> entities = new ArrayList<>();

    public int[] get_free_location_in_biome(Biome biome, int clearance) {
        outer:
        while (true) {
            int[] location_km = Custom_Math.double_to_int_2d(Custom_Math.random_position_2d(0, 0, max_x_km, max_y_km));
            if (biomes_km[location_km[0]][location_km[1]] == biome && kingdom_km[location_km[0]][location_km[1]] == null) {
                for (int i = location_km[0] - clearance; i <= location_km[0] + clearance; i++) {
                    for (int j = location_km[1] - clearance; j <= location_km[1] + clearance; j++) {
                        if (biomes_km[i][j] != biome || kingdom_km[i][j] != null) {
                            continue outer;
                        }
                    }
                }
                return location_km;
            }
        }
    }

    //only north has polar zones, we pretend the south is actually near equator
    private void assign_biomes_from_heightmap_km(Biome[][] biomes_km, int[][] heightmap_km, double[][] heightmap_10km) {
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                if (4000 < heightmap_km[x][y]) {
                    biomes_km[x][y] = Biome.NIVAL;
                } else if (2000 < heightmap_km[x][y]) {
                    biomes_km[x][y] = Biome.ALPINE;
                } else if (heightmap_10km[x / 10][y / 10] < ocean_biome_depth_qualify_m) {
                    biomes_km[x][y] = Biome.OCEAN;
                } else if ((double) 5 / 6 * max_y_km < y) {
                    biomes_km[x][y] = Biome.POLAR;
                } else if ((double) 3 / 4 * max_y_km < y) {
                    biomes_km[x][y] = Biome.TUNDRA;
                }
            }
        }

        //detect coasts and reefs
        for (int x = 3; x < max_x_km - 4; x++) {
            for (int y = 3; y < max_y_km - 4; y++) {
                if (-200 < heightmap_km[x][y] && heightmap_km[x][y] < 200) {
                    boolean ocean_near = false;
                    outer:
                    for (int local_x = x - 3; local_x <= x + 3; local_x++) {
                        for (int local_y = y - 3; local_y <= y + 3; local_y++) {
                            if (biomes_km[local_x][local_y] == Biome.OCEAN) {
                                ocean_near = true;
                                break outer;
                            }
                        }
                    }
                    if (ocean_near) {
                        if (heightmap_km[x][y] < 0) {
                            biomes_km[x][y] = Biome.REEF;
                        } else {
                            biomes_km[x][y] = Biome.COASTAL;
                        }
                    }
                }
            }
        }

        System.out.println("Assigned nival, alpine, ocean, polar, tundra, coast, reef");
    }

    private void copy_biome(int x, int y, Biome biome, Biome[][] biomes_km, Queue<Integer> frontier_x, Queue<Integer> frontier_y, Queue<Biome> frontier_biome) {
        if (biomes_km[x][y] == null) {
            biomes_km[x][y] = biome;
            frontier_x.add(x);
            frontier_y.add(y);
            frontier_biome.add(biome);
        }
    }

    private void assign_biomes_requiring_seed(Biome[][] biomes_km) {
        Queue<Integer> frontier_x = new ArrayDeque<>();
        Queue<Integer> frontier_y = new ArrayDeque<>();
        Queue<Biome> frontier_biome = new ArrayDeque<>();

        for (Biome biome_seed : biome_seeds) {
            while (true) {
                int[] location_km = {0};
                if (biome_seed == Biome.DESERT || biome_seed == Biome.TROPICAL) {
                    location_km = Custom_Math.double_to_int_2d(Custom_Math.random_position_2d(0, (double) 1 / 2 * max_y_km, max_x_km, max_y_km));
                } else if (biome_seed == Biome.TEMPERATE_GRASSLAND || biome_seed == Biome.TEMPERATE_FOREST || biome_seed == Biome.RIVER) {
                    location_km = Custom_Math.double_to_int_2d(Custom_Math.random_position_2d(0, (double) 1 / 4 * max_y_km, max_x_km, (double) 3 / 4 * max_y_km));
                }

                if (biomes_km[location_km[0]][location_km[1]] == null) {
                    biomes_km[location_km[0]][location_km[1]] = biome_seed;
                    frontier_x.add(location_km[0]);
                    frontier_y.add(location_km[1]);
                    frontier_biome.add(biome_seed);
                    break;
                }
            }
        }

        //expand the seeds
        while (!frontier_x.isEmpty()) {
            int x = frontier_x.poll();
            int y = frontier_y.poll();
            Biome biome = frontier_biome.poll();

            if (x == 0 || x == max_x_km - 1 || y == 0 || y == max_y_km) {
                continue;
            }

            if (biome == Biome.RIVER) {
                //rivers tend to go straight, and if not, then 50/50 each turn direction
                if (biomes_km[x - 1][y] == Biome.RIVER) {
                    if (Custom_Math.pseudo_random() > 0.2) {
                        copy_biome(x + 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                    } else {
                        if (Custom_Math.pseudo_random() < 0.5) {
                            copy_biome(x, y + 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        } else {
                            copy_biome(x, y - 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        }
                    }
                } else if (biomes_km[x + 1][y] == Biome.RIVER) {
                    if (Custom_Math.pseudo_random() > 0.2) {
                        copy_biome(x - 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                    } else {
                        if (Custom_Math.pseudo_random() < 0.5) {
                            copy_biome(x, y + 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        } else {
                            copy_biome(x, y - 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        }
                    }
                } else if (biomes_km[x][y - 1] == Biome.RIVER) {
                    if (Custom_Math.pseudo_random() > 0.2) {
                        copy_biome(x, y + 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                    } else {
                        if (Custom_Math.pseudo_random() < 0.5) {
                            copy_biome(x - 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        } else {
                            copy_biome(x + 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        }
                    }
                } else if (biomes_km[x][y + 1] == Biome.RIVER) {
                    if (Custom_Math.pseudo_random() > 0.2) {
                        copy_biome(x, y - 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                    } else {
                        if (Custom_Math.pseudo_random() < 0.5) {
                            copy_biome(x - 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        } else {
                            copy_biome(x + 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                        }
                    }
                }
            } else {
                //Desert, tropical, forest, grassland
                //50% chance to expand in all four directions
                if (Custom_Math.pseudo_random() < 0.5) {
                    copy_biome(x - 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                    copy_biome(x + 1, y, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                    copy_biome(x, y - 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                    copy_biome(x, y + 1, biome, biomes_km, frontier_x, frontier_y, frontier_biome);
                } else {
                    //add it back since it was polled
                    frontier_x.add(x);
                    frontier_y.add(y);
                    frontier_biome.add(biome);
                }
            }
        }

        System.out.println("Desert, tropical, forest, grassland seeded and expanded");
    }

    //custom shape for mountain
    //height in meter, x in kilometers
    //radius is only used to attenuate, since nearby boundary cells also generate mountains, which would be redundant heights
    public static double mountain_height_at_distance(int max_height, int grid_radius, double x) {
        return (max_height * Math.pow(1.1d, -x) / (grid_radius * grid_radius * 4));
    }

    //custom shape for lakes
    //depth should be a positive number
    //height in meters, halfwidth and distance in kilometers
    public static double lake_height_at_distance(int max_lake_depth, int lake_halfwidth, double distance) {
        return -max_lake_depth * Math.exp(-(Math.pow(distance / lake_halfwidth * 2, 2)));
    }

    //meter precision heights, 10km stride coordinate
    private double[][] generate_heightmap_tectonic_10km() {
        int[][] tectonic_centers_coordinate_km = new int[num_plates][2];
        for (int i = 0; i < num_plates; i++) {
            tectonic_centers_coordinate_km[i] = Custom_Math.double_to_int_2d(Custom_Math.random_position_2d(0, 0, max_x_km, max_y_km));
        }

        int[] tectonic_base_elevations_m = new int[num_plates];
        for (int i = 0; i < num_plates; i++) {
            tectonic_base_elevations_m[i] = (Custom_Math.pseudo_random() > 0.2) ? continental_height_m : ocean_depth_m;
        }

        //we create a terrain at 10km x 10km precision
        //but the values are in kilometers, so we divide by 10
        int grid_length = max_x_km / 10;
        int grid_width = max_y_km / 10;

        //assign each cell's tectonic plate by seeking the closest tectonic center (Voronoi)
        int[][] tectonic_belongings_10km = new int[grid_length][grid_width];
        for (int grid_x = 0; grid_x < grid_length; grid_x++) {
            for (int grid_y = 0; grid_y < grid_width; grid_y++) {
                double closest_dist_km = Double.MAX_VALUE;
                int plate_index = -1;
                for (int i = 0; i < num_plates; i++) {
                    double dist_km = Custom_Math.distance(grid_x * 10, grid_y * 10, tectonic_centers_coordinate_km[i][0], tectonic_centers_coordinate_km[i][1]);
                    if (dist_km < closest_dist_km) {
                        closest_dist_km = dist_km;
                        plate_index = i;
                    }
                }
                tectonic_belongings_10km[grid_x][grid_y] = plate_index;

                //plates on the map edge must be ocean
                if (grid_x == 0 || grid_y == 0 || grid_x == grid_length - 1 || grid_y == grid_width - 1) {
                    tectonic_base_elevations_m[plate_index] = ocean_depth_m;
                }
            }
        }

        double[][] heightmap_10km = new double[grid_length][grid_width];
        for (int grid_x = 0; grid_x < grid_length; grid_x++) {
            for (int grid_y = 0; grid_y < grid_width; grid_y++) {
                heightmap_10km[grid_x][grid_y] = tectonic_base_elevations_m[tectonic_belongings_10km[grid_x][grid_y]];
            }
        }

        double[][] tectonic_velocity = new double[num_plates][2];
        for (int i = 0; i < num_plates; i++) {
            tectonic_velocity[i] = Custom_Math.unit_random_vector_2d();
        }

        for (int grid_x = 1; grid_x < grid_length - 1; grid_x++) {
            for (int grid_y = 1; grid_y < grid_width - 1; grid_y++) {
                boolean is_tectonic_boundary = (tectonic_belongings_10km[grid_x - 1][grid_y] != tectonic_belongings_10km[grid_x + 1][grid_y] ||
                        tectonic_belongings_10km[grid_x - 1][grid_y - 1] != tectonic_belongings_10km[grid_x + 1][grid_y + 1] ||
                        tectonic_belongings_10km[grid_x][grid_y - 1] != tectonic_belongings_10km[grid_x][grid_y + 1] ||
                        tectonic_belongings_10km[grid_x + 1][grid_y - 1] != tectonic_belongings_10km[grid_x - 1][grid_y + 1]
                );

                if (is_tectonic_boundary) {
                    int belonging1 = tectonic_belongings_10km[grid_x][grid_y];
                    int belonging2 = -1;
                    for (int i = grid_x - 1; i <= grid_x + 1; i++) {
                        for (int j = grid_y - 1; j <= grid_y + 1; j++) {
                            if (tectonic_belongings_10km[i][j] != belonging1) {
                                belonging2 = tectonic_belongings_10km[i][j];
                                break;
                            }
                        }
                    }

                    double[] center_to_center_dir = Custom_Math.unit_vector_2d_from(
                            Custom_Math.difference_vector_2d(
                                    tectonic_centers_coordinate_km[belonging2],
                                    tectonic_centers_coordinate_km[belonging1]
                            )
                    );
                    double[] relative_motion = Custom_Math.difference_vector_2d(tectonic_velocity[belonging2], tectonic_velocity[belonging1]);
                    double dot = Custom_Math.dot_2d(center_to_center_dir, relative_motion);
                    //dot < 0 is convergent
                    for (int x_local = Math.max(0, grid_x - mountain_width_km / 10); x_local <= Math.min(grid_length - 1, grid_x + mountain_width_km / 10); x_local++) {
                        for (int y_local = Math.max(0, grid_y - mountain_width_km / 10); y_local <= Math.min(grid_width - 1, grid_y + mountain_width_km / 10); y_local++) {
                            double distance = Custom_Math.distance(grid_x * 10, grid_y * 10, x_local * 10, y_local * 10);
                            heightmap_10km[x_local][y_local] += (-dot) * mountain_height_at_distance(mountain_max_height_m, mountain_width_km / 10, distance);
                        }
                    }
                }
            }
        }

        //mainly smooths elevation difference between continental and oceanic plates
        for (int i = 0; i < 10; i++) {
            Custom_Math.average_smooth_heightmap(heightmap_10km);
        }

        System.out.println("10km heightmap generated");

        return heightmap_10km;
    }

    //heights in meters, 1km stride coordinates
    private int[][] sample_km_from_10km(double[][] heightmap_10km) {
        int[][] heightmap_km = new int[max_x_km][max_y_km];
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                heightmap_km[x][y] = (int) heightmap_10km[x / 10][y / 10];
            }
        }

        System.out.println("Sampled km heightmap from 10km heightmap");

        return heightmap_km;
    }

    private void spawn_volcano(int[][] heightmap_km) {
        int[] volcano_location = Custom_Math.double_to_int_2d(Custom_Math.random_position_2d(
                (double) 1 / 4 * max_x_km,
                (double) 1 / 4 * max_y_km,
                (double) 3 / 4 * max_x_km,
                (double) 3 / 4 * max_y_km
        ));
        int height = (int) (Custom_Math.pseudo_random() * 5 * mountain_max_height_m);

        for (int x = volcano_location[0] - mountain_width_km; x <= volcano_location[0] + mountain_width_km; x++) {
            for (int y = volcano_location[1] - mountain_width_km; y <= volcano_location[1] + mountain_width_km; y++) {
                int distance = Custom_Math.distance(volcano_location[0], volcano_location[1], x, y);
                heightmap_km[x][y] += (int) mountain_height_at_distance(height, 1, distance);
            }
        }

        System.out.println("Volcano spawned");
    }

    private void spawn_lakes(int[][] heightmap_km, Biome[][] biomes_km) {
        double[][] added_heightmap = new double[max_x_km][max_y_km];
        for (int i = 0; i < num_lakes; i++) {
            int[] location_km = Custom_Math.double_to_int_2d(Custom_Math.random_position_2d(
                    (double) 1 / 4 * max_x_km,
                    (double) 1 / 4 * max_y_km,
                    (double) 3 / 4 * max_x_km,
                    (double) 3 / 4 * max_y_km
            ));
            int width_km = (int) (Custom_Math.pseudo_random() * lake_max_width_km);

            for (int x = location_km[0] - width_km; x <= location_km[0] + width_km; x++) {
                for (int y = location_km[1] - width_km; y <= location_km[1] + width_km; y++) {
                    int distance = Custom_Math.distance(location_km[0], location_km[1], x, y);
                    added_heightmap[x][y] += lake_height_at_distance(lake_depth_m, width_km, distance);
                    biomes_km[x][y] = Biome.LAKE;
                }
            }
        }
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                heightmap_km[x][y] += (int) added_heightmap[x][y];
            }
        }

        System.out.println("Lakes excavated from km heightmap");
    }

    public Earth() {
        double[][] heightmap_10km = generate_heightmap_tectonic_10km();

        int[][] heightmap_km = sample_km_from_10km(heightmap_10km);
        Custom_Math.add_perlin(heightmap_km, 1000, 0, 0, max_x_km, 1000);
        Custom_Math.add_perlin(heightmap_km, 500, 0, 0, max_x_km, 500);
        Custom_Math.add_perlin(heightmap_km, 100, 0, 0, max_x_km, 100);

        Biome[][] biomes_km = new Biome[max_x_km][max_y_km];
        spawn_lakes(heightmap_km, biomes_km);
        spawn_volcano(heightmap_km);
        assign_biomes_from_heightmap_km(biomes_km, heightmap_km, heightmap_10km);

        this.heightmap_km = heightmap_km;
        this.biomes_km = biomes_km;

        this.assign_biomes_requiring_seed(biomes_km);

        eldoria = new Eldoria(this);

        entities.addLast(new Player());
        System.out.println("Human player Ming added");

        System.out.println("WORLD GENERATION DONE");
    }
}
