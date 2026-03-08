package minggrosfou.eldoria.city;

public class Willowshade {
    public static int[] coord_km;

    public Willowshade(Luminara luminara) {
        //pray that it's not an ocean or smt
        coord_km = new int[]{luminara.coord_km[0] - 10, luminara.coord_km[1] - 10};
    }
}
