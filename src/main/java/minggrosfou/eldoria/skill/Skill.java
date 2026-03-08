package minggrosfou.eldoria.skill;

public abstract class Skill {
    public Type type;
    public String name;
    public String description;
    public double CD_s;
    public int MP_cost;
    public int level = 1;

    public enum Type {
        ACTIVE,
        PASSIVE,
        TOGGLE_PASSIVE
    }
}
