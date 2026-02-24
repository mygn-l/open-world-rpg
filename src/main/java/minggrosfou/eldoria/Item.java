package minggrosfou.eldoria;

public class Item {
    Type type = Type.UNKNOWN;

    String name = "?";
    String description;

    boolean equippable = false;

    int STR_boost = 0;
    int DEX_boost = 0;
    int VIT_boost = 0;
    int INT_boost = 0;
    int WIS_boost = 0;
    int CHA_boost = 0;
    double weight = 1;
    double volume = 1;

    public enum Type {
        AMMO,
        ARM,
        BACK,
        BAG,
        COIN,
        DOCUMENT,
        EGG,
        FACEWEAR,
        FEET,
        FOOD,
        HEAD,
        HERB,
        KEY,
        LEATHER,
        LEG,
        LOWER,
        MAP,
        MELEE,
        MONSTER_PART,
        NECK,
        ORE,
        POTION,
        RANGED,
        RING,
        SCROLL,
        SHIELD,
        STAFF,
        THROWABLE,
        TOOL,
        TRAP,
        UNKNOWN,
        UPPER,
        VALUABLE,
        WOOD,
        WRIST
    }
}
