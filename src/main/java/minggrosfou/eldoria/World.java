package minggrosfou.eldoria;

import minggrosfou.eldoria.kingdoms.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class World {
    //min x = 0, min y = 0
    //distances in km, heights in meter
    public static final int max_x_km = 5200; //west = 0, east = 5200
    public static final int max_y_km = 5500; //north = 5500, south = 0

    private static final int num_plates = 20;

    private static final int ocean_depth_m = -5000;
    private static final int continental_height_m = 1000;

    public static final int mountain_width_km = 100;
    private static final int mountain_max_height_m = 20000;

    private static final int num_volcanoes = 2;

    private static final int num_lakes = 2;
    private static final int lake_max_width_km = 500;
    private static final int lake_depth_m = 1000;

    private static final int num_desert_seeds = 3;
    private static final int num_tropical_seeds = 5;
    private static final int num_forest_seeds = 10;
    private static final int num_grassland_seeds = 10;
    private static final int num_savanna_seeds = 2;
    private static final int num_river_seeds = 10;
    private static final int num_wetland_seeds = 2;

    private final Queue<Integer> frontier_x = new ArrayDeque<>();
    private final Queue<Integer> frontier_y = new ArrayDeque<>();
    private final Queue<Biome> frontier_biome = new ArrayDeque<>();

    //10km precision horizontal, 1m precision vertical
    private int[][] tectonic_centers;
    private int[][] tectonic_belongings_10km;
    private int[] tectonic_elevations;
    private int[][] heightmap_10km;

    //1km precision horizontal, 1m precision vertical
    public int[][] heightmap_km;

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

    public Biome[][] biomes_km = new Biome[max_x_km][max_y_km];

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
        SAVANNA_STOP(9),
        SILVERREACH(10),
        UNITED_HUMAN_DEMON_REALM(11),
        WORLD_TREE(12);

        private final int id;

        Kingdom(int id) {
            this.id = id;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public int getId() {
            return id;
        }
    }

    public Kingdom[][] kingdom_belongings_km = new Kingdom[max_x_km][max_y_km];

    //public final Azureport_Republic azureport_republic;
    //public final Beastkin_Confederacy beastkin_confederacy;
    //public final Desert_Stop[] desert_stops;
    //public final Dragon_Lair dragon_lair;
    //public final Dwarven_Kingdom dwarven_kingdom;
    public final Eldoria eldoria;
    //public final Gnome_Technomancy_Enclave_Sparkhaven gnome_technomancy_enclave_sparkhaven;
    //public final Ironwall_Hold ironwall_hold;
    //public final Merfolk_Kingdom merfolk_kingdom;
    //public final Savanna_Stop savanna_stop;
    //public final Silverreach silverreach;
    public United_Human_Demon_Realm united_human_demon_realm;
    //public final World_Tree world_tree;

    public ArrayList<Player> players = new ArrayList<>();

    public int[] get_free_location_in_biome(Biome biome, int clearance, int dist_from_world_edge) {
        outer:
        while (true) {
            int location_x = (int) (Math.random() * max_x_km);
            int location_y = (int) (Math.random() * max_y_km);
            if (location_x < dist_from_world_edge || location_x >= max_x_km - dist_from_world_edge || location_y < dist_from_world_edge || location_y >= max_y_km - dist_from_world_edge) {
                continue;
            }
            if (biomes_km[location_x][location_y] == biome && kingdom_belongings_km[location_x][location_y] == null) {
                for (int i = location_x - clearance; i <= location_x + clearance; i++) {
                    for (int j = location_y - clearance; j <= location_y + clearance; j++) {
                        if (biomes_km[i][j] != biome || kingdom_belongings_km[i][j] != null) {
                            continue outer;
                        }
                    }
                }
                return new int[]{location_x, location_y};
            }
        }
    }

    private void assign_mountainous_zone() {
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                if (heightmap_km[x][y] > 4000) {
                    biomes_km[x][y] = Biome.NIVAL;
                } else if (heightmap_km[x][y] > 2000) {
                    biomes_km[x][y] = Biome.ALPINE;
                }
            }
        }
    }

    private void assign_aquatic_zone() {
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                if (tectonic_elevations[tectonic_belongings_10km[x / 10][y / 10]] < 0) {
                    if (heightmap_km[x][y] < -200) {
                        biomes_km[x][y] = Biome.OCEAN;
                    } else if (heightmap_km[x][y] < 0) {
                        biomes_km[x][y] = Biome.REEF;
                    }
                } else {
                    if (heightmap_km[x][y] < 0) {
                        biomes_km[x][y] = Biome.LAKE;
                    }
                }
            }
        }
    }

    private void assign_polar_zone() {
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km / 6; y++) {
                if (biomes_km[x][y] == null) {
                    biomes_km[x][y] = Biome.POLAR;
                }
            }
            for (int y = max_y_km / 6; y < max_y_km / 5; y++) {
                if (biomes_km[x][y] == null) {
                    biomes_km[x][y] = Biome.TUNDRA;
                }
            }

            for (int y = (int) (5d / 6d * max_y_km); y < max_y_km; y++) {
                if (biomes_km[x][y] == null) {
                    biomes_km[x][y] = Biome.POLAR;
                }
            }
            for (int y = (int) (4d / 5d * max_y_km); y < (int) (5d / 6d * max_y_km); y++) {
                if (biomes_km[x][y] == null) {
                    biomes_km[x][y] = Biome.TUNDRA;
                }
            }
        }
    }

    private void assign_coastal_zone() {
        for (int x = 50; x < max_x_km - 50; x++) {
            for (int y = 50; y < max_y_km - 50; y++) {
                int water_count = 0;
                int land_count = 0;
                for (int i = x - 5; i < x + 5; i++) {
                    for (int j = y - 5; j < y + 5; j++) {
                        if (heightmap_km[i][j] < 0) {
                            water_count++;
                        } else {
                            land_count++;
                        }
                    }
                }
                if (Math.abs(water_count - land_count) < 6) {
                    biomes_km[x][y] = Biome.COASTAL;
                }
            }
        }
    }

    private void seed_desert() {
        for (int i = 0; i < num_desert_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.4 + 0.2) * max_y_km);

                if (biomes_km[location_x][location_y] == null) {
                    int min_dist = 10;
                    for (int x = Math.max(0, location_x - 10); x < Math.min(max_x_km, location_x + 10); x++) {
                        for (int y = Math.max(0, location_y - 10); y < Math.min(max_y_km, location_y + 10); y++) {
                            if (heightmap_km[x][y] < 0) {
                                int dist = Custom_Math.distance(x, y, location_x, location_y);
                                if (dist < min_dist) {
                                    min_dist = dist;
                                }
                            }
                        }
                    }
                    if (Math.random() < Math.pow((double) min_dist / 10, 2)) {
                        found_good_place = true;
                        biomes_km[location_x][location_y] = Biome.DESERT;
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

                if (biomes_km[location_x][location_y] == null) {
                    int min_dist = 10;
                    for (int x = Math.max(0, location_x - 10); x < Math.min(max_x_km, location_x + 10); x++) {
                        for (int y = Math.max(0, location_y - 10); y < Math.min(max_y_km, location_y + 10); y++) {
                            if (biomes_km[x][y] == Biome.DESERT) {
                                int dist = Custom_Math.distance(x, y, location_x, location_y);
                                if (dist < min_dist) {
                                    min_dist = dist;
                                }
                            }
                        }
                    }
                    if (Math.random() > Math.pow((double) min_dist / 10, 2)) {
                        found_good_place = true;
                        biomes_km[location_x][location_y] = Biome.SAVANNA;
                        frontier_x.add(location_x);
                        frontier_y.add(location_y);
                        frontier_biome.add(Biome.SAVANNA);
                    }
                }
            }
        }
    }

    private void seed_tropical() {
        for (int i = 0; i < num_tropical_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.35 + 0.3) * max_y_km);

                if (biomes_km[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes_km[location_x][location_y] = Biome.TROPICAL;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.TROPICAL);
                }
            }
        }
    }

    private void seed_forest_and_grassland() {
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

                if (biomes_km[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes_km[location_x][location_y] = Biome.TEMPERATE_FOREST;
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

                if (biomes_km[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes_km[location_x][location_y] = Biome.TEMPERATE_GRASSLAND;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.TEMPERATE_GRASSLAND);
                }
            }
        }
    }

    private void seed_wetland_and_river() {
        for (int i = 0; i < num_wetland_seeds; i++) {
            boolean found_good_place = false;
            while (!found_good_place) {
                int location_x = (int) (Math.random() * max_x_km);
                int location_y = (int) ((Math.random() * 0.2 + 0.6) * max_y_km);

                if (biomes_km[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes_km[location_x][location_y] = Biome.WETLAND;
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

                if (biomes_km[location_x][location_y] == null) {
                    found_good_place = true;
                    biomes_km[location_x][location_y] = Biome.RIVER;
                    frontier_x.add(location_x);
                    frontier_y.add(location_y);
                    frontier_biome.add(Biome.RIVER);
                }
            }
        }
    }

    private void copy_biome(int x, int y, Biome biome) {
        if (biomes_km[x][y] == null) {
            biomes_km[x][y] = biome;
            frontier_x.add(x);
            frontier_y.add(y);
            frontier_biome.add(biome);
        }
    }

    private void expand_seeds() {
        while (!frontier_x.isEmpty()) {
            int x = frontier_x.poll();
            int y = frontier_y.poll();
            Biome biome = frontier_biome.poll();

            if (!(x > 0 && x < max_x_km - 1 && y > 0 && y < max_y_km)) {
                continue;
            }

            if (biome == Biome.RIVER) {
                if (biomes_km[x - 1][y] == Biome.RIVER) {
                    if (Math.random() > 0.2) {
                        copy_biome(x + 1, y, biome);
                    } else {
                        if (Math.random() < 0.5) {
                            copy_biome(x, y + 1, biome);
                        } else {
                            copy_biome(x, y - 1, biome);
                        }
                    }
                } else if (biomes_km[x + 1][y] == Biome.RIVER) {
                    if (Math.random() > 0.2) {
                        copy_biome(x - 1, y, biome);
                    } else {
                        if (Math.random() < 0.5) {
                            copy_biome(x, y + 1, biome);
                        } else {
                            copy_biome(x, y - 1, biome);
                        }
                    }
                } else if (biomes_km[x][y - 1] == null) {
                    if (Math.random() > 0.2) {
                        copy_biome(x, y + 1, biome);
                    } else {
                        if (Math.random() < 0.5) {
                            copy_biome(x - 1, y, biome);
                        } else {
                            copy_biome(x + 1, y, biome);
                        }
                    }
                } else if (biomes_km[x][y + 1] == null) {
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
                if (Math.random() > 0.01) {
                    copy_biome(x - 1, y, biome);
                }
                if (Math.random() > 0.01) {
                    copy_biome(x + 1, y, biome);
                }
                if (Math.random() > 0.01) {
                    copy_biome(x, y - 1, biome);
                }
                if (Math.random() > 0.01) {
                    copy_biome(x, y + 1, biome);
                }
            } else {
                if (Math.random() < 0.5) {
                    copy_biome(x - 1, y, biome);
                    copy_biome(x + 1, y, biome);
                    copy_biome(x, y - 1, biome);
                    copy_biome(x, y + 1, biome);
                } else {
                    frontier_x.add(x);
                    frontier_y.add(y);
                    frontier_biome.add(biome);
                }
            }
        }
    }

    private void generate_rough_tectonic_heightmap() {
        tectonic_centers = new int[num_plates][2];
        for (int i = 0; i < num_plates; i++) {
            tectonic_centers[i][0] = (int) (Math.random() * max_x_km);
            tectonic_centers[i][1] = (int) (Math.random() * max_y_km);
        }

        tectonic_elevations = new int[num_plates];
        for (int i = 0; i < num_plates; i++) {
            if (Math.random() > 0.2) {
                tectonic_elevations[i] = continental_height_m;
            } else {
                tectonic_elevations[i] = ocean_depth_m;
            }
        }

        tectonic_belongings_10km = new int[max_x_km / 10][max_y_km / 10];
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
                tectonic_belongings_10km[x][y] = associated_index;

                if (x == 0 || y == 0 || x == max_x_km / 10 - 1 || y == max_y_km / 10 - 1) {
                    tectonic_elevations[associated_index] = ocean_depth_m;
                }
            }
        }

        heightmap_10km = new int[max_x_km / 10][max_y_km / 10];
        for (int x = 0; x < max_x_km / 10; x++) {
            for (int y = 0; y < max_y_km / 10; y++) {
                heightmap_10km[x][y] = tectonic_elevations[tectonic_belongings_10km[x][y]];
            }
        }
    }

    private void generate_mountains_and_rifts_from_rough() {
        double[][] tectonic_movement = new double[num_plates][2];
        for (int i = 0; i < num_plates; i++) {
            double mag = Math.random();
            double dir = Math.random() * Math.PI * 2;
            tectonic_movement[i][0] = mag * Math.cos(dir);
            tectonic_movement[i][1] = mag * Math.sin(dir);
        }

        //divide, because we need 10km version, since rough_tectonic is 10km precision
        for (int x = mountain_width_km / 10; x < (max_x_km - mountain_width_km) / 10; x++) {
            for (int y = mountain_width_km / 10; y < (max_y_km - mountain_width_km) / 10; y++) {
                boolean is_tectonic_boundary = !(
                        tectonic_belongings_10km[x - 1][y] == tectonic_belongings_10km[x + 1][y] &&
                                tectonic_belongings_10km[x - 1][y - 1] == tectonic_belongings_10km[x + 1][y + 1] &&
                                tectonic_belongings_10km[x][y - 1] == tectonic_belongings_10km[x][y + 1] &&
                                tectonic_belongings_10km[x + 1][y - 1] == tectonic_belongings_10km[x - 1][y + 1]
                );

                if (is_tectonic_boundary) {
                    int belonging1 = tectonic_belongings_10km[x][y];
                    int belonging2 = -1;
                    for (int i = x - 1; i <= x + 1; i++) {
                        for (int j = y - 1; j <= y + 1; j++) {
                            if (tectonic_belongings_10km[i][j] != belonging1) {
                                belonging2 = tectonic_belongings_10km[i][j];
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
                            heightmap_10km[x_local][y_local] += (-dot) * Custom_Math.mountain_increment(mountain_max_height_m, mountain_width_km / 10, distance);
                        }
                    }
                }
            }
        }
    }

    private void sample_km_from_10km() {
        heightmap_km = new int[max_x_km][max_y_km];
        for (int x = 0; x < max_x_km; x++) {
            for (int y = 0; y < max_y_km; y++) {
                heightmap_km[x][y] = heightmap_10km[x / 10][y / 10];
            }
        }
    }

    private void spawn_volcanoes_and_united_human_demon_realm() {
        for (int i = 0; i < num_volcanoes; i++) {
            int location_x = (int) ((Math.random() * 0.5 + 0.25) * max_x_km);
            int location_y = (int) ((Math.random() * 0.5 + 0.25) * max_y_km);
            int height = (int) ((2 + Math.random()) * mountain_max_height_m);

            for (int x = location_x - mountain_width_km; x < location_x + mountain_width_km; x++) {
                for (int y = location_y - mountain_width_km; y < location_y + mountain_width_km; y++) {
                    int distance = Custom_Math.distance(location_x, location_y, x, y);
                    heightmap_km[x][y] += (int) Math.ceil(Custom_Math.mountain_increment(height, 1, distance));
                }
            }

            if (i == 0) {
                united_human_demon_realm = new United_Human_Demon_Realm(this, location_x, location_y);
            }
        }
    }

    private void spawn_lakes() {
        for (int i = 0; i < num_lakes; i++) {
            int location_x = (int) ((Math.random() * 0.5 + 0.25) * max_x_km);
            int location_y = (int) ((Math.random() * 0.5 + 0.25) * max_y_km);
            int width = (int) (Math.random() * lake_max_width_km);

            for (int x = location_x - width; x < location_x + width; x++) {
                for (int y = location_y - width; y < location_y + width; y++) {
                    int distance = Custom_Math.distance(location_x, location_y, x, y);
                    heightmap_km[x][y] += (int) Math.ceil(Custom_Math.lake_increment(lake_depth_m, width, distance));
                }
            }
        }
    }

    public World() {
        this.generate_rough_tectonic_heightmap();
        for (int i = 0; i < 10; i++) {
            Custom_Math.average_smooth_heightmap(heightmap_10km);
        }
        System.out.println("Tectonic heights done");

        this.generate_mountains_and_rifts_from_rough();
        System.out.println("Mountains done");

        this.sample_km_from_10km();
        System.out.println("Sampled km from 10km");

        Custom_Math.add_perlin(heightmap_km, 1000, 0, 0, 5000, 5000, 1000, (long) (Math.random() * 50));
        System.out.println("Perlin 1000km done");

        Custom_Math.add_perlin(heightmap_km, 500, 0, 0, 5000, 5000, 500, (long) (Math.random() * 50));
        System.out.println("Perlin 500km done");

        this.spawn_lakes();
        System.out.println("Lakes done");

        this.spawn_volcanoes_and_united_human_demon_realm();
        System.out.println("Volcanoes done");
        System.out.println("United Human-Demon Realm spawned");

        Custom_Math.add_perlin(heightmap_km, 100, 0, 0, 5000, 5000, 100, (long) (Math.random() * 50));
        System.out.println("Perlin 100km done");

        this.assign_aquatic_zone();
        System.out.println("Aquatic done");
        this.assign_polar_zone();
        System.out.println("Polar done");
        this.assign_coastal_zone();
        System.out.println("Coastal done");
        this.assign_mountainous_zone();
        System.out.println("Mountainous done");

        this.seed_desert();
        System.out.println("Desert seeded");
        this.seed_tropical();
        System.out.println("Tropical seeded");
        this.seed_forest_and_grassland();
        System.out.println("Temperate forest and grassland seeded");
        this.seed_wetland_and_river();
        System.out.println("Wetland and river seeded");

        this.expand_seeds();
        System.out.println("Seeds expanded");

        //human-demon realm is spawned by volcano method
        eldoria = new Eldoria(this);
        //beastkin_confederacy = new Beastkin_Confederacy(this);
        //gnome_technomancy_enclave_sparkhaven = new Gnome_Technomancy_Enclave_Sparkhaven(this);
        //azureport_republic = new Azureport_Republic(this);
        //silverreach = new Silverreach(this);
        //dwarven_kingdom = new Dwarven_Kingdom(this);
        //ironwall_hold = new Ironwall_Hold(this);
        //world_tree = new World_Tree(this);
        //dragon_lair = new Dragon_Lair(this);
        //merfolk_kingdom = new Merfolk_Kingdom(this);
        //desert_stops = new Desert_Stop[3];
        //desert_stops[0] = new Desert_Stop(this);
        //desert_stops[1] = new Desert_Stop(this);
        //desert_stops[2] = new Desert_Stop(this);
        //savanna_stop = new Savanna_Stop(this);
        System.out.println("Eldoria spawned");

        players.addLast(new Player("Ming", Player.Race.HUMAN));
        System.out.println("Human player Ming added");

        System.out.println("WORLD GENERATION DONE");
    }
}
