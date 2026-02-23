package minggrosfou.eldoria;

import java.util.ArrayList;
import java.util.Map;

public class Player {
    String name = "Ming";
    String origin_city = "Willowshade";
    String origin_kingdom = "Eldoria";
    String backstory = "Reincarnated in Eldoria.";
    ArrayList<String> personality;

    Race race;
    Combat_Class combat_class = Combat_Class.NONE;
    String specialization;
    String job;

    int level = 1;
    public static int max_level = 100;
    int EXP = 0;

    int STR = 5;
    int DEX = 5;
    int VIT = 5;
    int INT = 5;
    int WIS = 5;
    int CHA = 5;
    int unallocated_stat_points = 0;

    int HP = max_hp();
    int MP = max_mp();
    double gauge = 0;

    Item main_hand;
    Item off_hand;
    Item upper_inner;
    Item upper_outer;
    Item back;
    Item lower_outer;
    Item lower_inner;
    Item head;
    Item face1;
    Item face2;
    Item ring_left;
    Item ring_right;
    Item wrist;
    Item neck;
    Item arm_left;
    Item arm_right;
    Item leg_left;
    Item leg_right;
    Item feet;

    Inventory inventory = new Inventory();

    Map<Player, Relationship> relationships;

    double world_x = 2500d;
    double world_y = 2500d;

    public static enum Race {
        BEASTKIN,
        DEMON,
        DWARF,
        ELF,
        FAIRY,
        GNOME,
        GOBLIN,
        HALFLING,
        HUMAN,
        MERFOLK,
        ORC
    }

    public static enum Combat_Class {
        ARCHER,
        ASSASSIN,
        BATTLEMAGE,
        BERSERKER,
        DEFENDER,
        HEALER,
        LANCER,
        MAGE,
        MONK,
        NONE,
        SHAMAN,
        SUMMONER,
        SWORDSMAN
    }

    public static enum Job {
        ACTOR,
        ADVENTURER,
        ALCHEMIST,
        APOTHECARY,
        APPRAISER,
        ARCHITECT,
        ARTIFACT_ARTISAN,
        BAKER,
        BANKER,
        BARBER,
        BARTENDER,
        BEGGAR,
        BLACKSMITH,
        BREEDER,
        BREWER,
        BUTCHER,
        CARAVANEER,
        CARPENTER,
        CHEF,
        CLERK,
        CONSTRUCTION_WORKER,
        ENGINEER,
        EXTERMINATOR,
        FARMER,
        FISHER,
        FURNITURE_ARTISAN,
        GARDENER,
        GATHERER,
        GLASSBLOWER,
        GRAVE_DIGGER,
        GUARD,
        GUILD_MASTER,
        HEALER,
        HISTORIAN,
        HUNTER,
        INNKEEPER,
        ITEM_ENCHANTER,
        JEWELER,
        LIBRARIAN,
        LUMBERJACK,
        LUTHIER,
        MAILMAN,
        MAYOR,
        MERCHANT,
        MINER,
        NURSE,
        PAINTER,
        PLUMBER,
        PORTER,
        POTTER,
        PRIEST,
        SAILOR,
        SCROLL_SCRIBE,
        SCULPTOR,
        SECRETARY,
        SERVANT,
        SHEPHERD,
        SHOEMAKER,
        SHOPKEEPER,
        SOLDIER,
        STONEMASON,
        STREET_CLEANER,
        TAILOR,
        TAMER,
        TAX_COLLECTOR,
        TEACHER,
        THEATER_WORKER,
        TRAIN_CONDUCTOR,
        TRAPPER,
        UNEMPLOYED,
        VETERINARIAN,
        WAITRESS,
        WEAPON_ENCHANTER,
        WRITER
    }

    public Player(String name, Race race) {
        this.name = name;
        this.race = race;
    }

    public int exp_required() {
        return (int) Math.round(17d * Math.pow(Math.log(level), 1.5d));
    }

    public int stat_point_gain() {
        return (int) Math.ceil(Math.log(level));
    }

    public int max_hp() {
        return (int) Math.ceil(5 * level + 5d * Math.pow(VIT, 1.5d));
    }

    public int max_mp() {
        return (int) Math.ceil(2 * level + 4d * Math.pow(INT + WIS, 1.5d));
    }

    public int hp_regen() {
        return (int) Math.ceil(Math.log(level) + VIT);
    }

    public int mp_regen() {
        return (int) Math.ceil(2 * Math.log(level) + Math.pow(INT + WIS - 5, 1.3));
    }

    public double gauge_increase_per_tick() {
        return ((double) Math.round((0.02 * Math.log(level) + 0.1 * Math.pow(DEX, 0.7)) * 100) / 100);
    }

    public double dodge_chance() {
        return Math.max(0, 0.2 + 0.1 * ((double) level / (level + 20)) + 0.7 * ((double) DEX / (DEX + 20)) + 0.2 * ((double) STR / (STR + 100)) - Math.log(VIT - 4) / 5);
    }
}
