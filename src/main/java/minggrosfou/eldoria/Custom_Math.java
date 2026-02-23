package minggrosfou.eldoria;

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
    public static double logistic_falloff(double starting_height, double ending_height, double x) {
        double diff = ending_height - starting_height;
        double k = 0.1d / diff;
        double R = -50d * diff;
        return diff / (1 + Math.exp(k * (x - R))) + starting_height;
    }
    public static double mountain_increment(int max_height, int mountain_width, int x) {
        return (max_height * Math.pow(1.1d, -x) / (mountain_width * mountain_width * 4));
    }
    public static double lake_increment(int lake_depth, int lake_width, double distance) {
        return -lake_depth * Math.exp(-(Math.pow(distance / lake_width * 2, 2)));
    }
    public static double fade(double t) {
        return (6 * Math.pow(t, 5) - 15 * Math.pow(t, 4) + 10 * Math.pow(t, 3));
    }
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
