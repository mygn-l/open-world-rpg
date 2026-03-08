package minggrosfou.eldoria.entity;

import minggrosfou.eldoria.city.Willowshade;
import minggrosfou.eldoria.world.Earth;

import java.util.ArrayList;
import java.util.Map;

public class Player extends Entity{
    String origin_city = "Willowshade";
    Earth.Kingdom origin_kingdom = Earth.Kingdom.ELDORIA;
    ArrayList<String> personality;

    public enum Combat_Class {
        NONE,
        SWORDSMAN,
        LANCER,
        BERSERKER,
        MONK,
        ASSASSIN,
        ARCHER,
        MARKSMAN,
        FIRE_MAGE,
        NATURE_MAGE,
        WATER_ICE_MAGE,
        LIGHTNING_MAGE,
        EARTH_MAGE,
        WIND_MAGE,
        BEASTMASTER,
        ILLUSIONIST,
        DRUID,
        HEALER,
        SUPPORTER,
        DEFENDER,
        JUGGERNAUT
    }

    public enum Job {
        UNEMPLOYED,
        ADVENTURER
    }

    Combat_Class combat_class = Combat_Class.NONE;
    String specialization;
    Job job = Job.UNEMPLOYED;

    public enum Level_Tier {
        NOVICE,
        VETERAN,
        ELITE,
        LEGENDARY
    }

    public static int max_level = 100;
    int EXP = 0;

    int unallocated_stat_points = 0;

    Map<Entity, Relationship> relationships;

    public Player() {
        super("Unnamed", Race.HUMAN);

        backstory = "Reincarnated in Willowshade, Eldoria.";

        level = 1;

        this.world_cm[0] = Willowshade.coord_km[0] * 100000;
        this.world_cm[1] = Willowshade.coord_km[1] * 100000;
    }

    public int exp_required() {
        return (int) Math.round(17d * Math.pow(Math.log(level), 1.5d));
    }

    public int stat_point_gain() {
        return (int) Math.ceil(Math.log(level));
    }

    public void level_up() {
        level++;
        unallocated_stat_points += stat_point_gain();
    }

    public Level_Tier get_level_tier() {
        if (level < 20) {
            return Level_Tier.NOVICE;
        } else if (level < 50) {
            return Level_Tier.NOVICE;
        } else if (level < 100) {
            return Level_Tier.NOVICE;
        } else {
            return Level_Tier.LEGENDARY;
        }
    }
}
