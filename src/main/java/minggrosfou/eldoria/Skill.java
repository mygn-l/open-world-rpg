package minggrosfou.eldoria;

public class Skill {
    Type type;
    String name;
    String description;
    double CD_tick;
    int MP_cost;

    public static enum Type {
        ACTIVE,
        PASSIVE,
        TOGGLE_PASSIVE
    }
}
