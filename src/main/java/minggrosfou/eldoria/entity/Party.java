package minggrosfou.eldoria.entity;

import java.util.ArrayList;

public class Party {
    ArrayList<Entity> members = new ArrayList<>();
    Rank rank;

    public enum Rank {
        IRON,
        BRONZE,
        SILVER,
        GOLD,
        PLATINUM,
        DIAMOND,
        LEGEND
    }
}
