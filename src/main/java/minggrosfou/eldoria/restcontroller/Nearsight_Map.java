package minggrosfou.eldoria.restcontroller;

public class Nearsight_Map {
    public double[][] heights;
    public int[] nearsight_bottom_left_m;
    public double[] player_world_cm;

    public Nearsight_Map(double[][] heights, int[] nearsight_bottom_left_m, double[] player_world_cm) {
        this.heights = heights;
        this.nearsight_bottom_left_m = nearsight_bottom_left_m;
        this.player_world_cm = player_world_cm;
    }
}
