package minggrosfou.eldoria.entity;

import minggrosfou.eldoria.Custom_Math;

public class Relationship {
    public static final int max_level = 7;

    Entity entity1;
    Entity entity2;

    int EXP = 0;
    int level = 1;

    public int exp_required() {
        return (int) Math.round(Custom_Math.factorial(level) + Math.pow(2d, level) + 3 * level * level + 4 * level + 5);
    }
}
