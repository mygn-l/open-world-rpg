package minggrosfou.eldoria;

public class Relationship {
    public static final int max_level = 7;

    Player player1;
    Player player2;

    int EXP = 0;
    int level = 1;

    public int exp_required() {
        return (int) Math.round(Custom_Math.factorial(level) + Math.pow(2d, level) + 3 * level * level + 4 * level + 5);
    }
}
