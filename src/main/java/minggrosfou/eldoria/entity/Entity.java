package minggrosfou.eldoria.entity;

import minggrosfou.eldoria.skill.Skill;

import java.util.ArrayList;

public abstract class Entity {
    public String name;
    public String backstory;

    public Race race;

    public int level;

    int STR = 5;
    int DEX = 5;
    int VIT = 5;
    int INT = 5;
    int WIS = 5;
    int CHA = 5;

    double HP = max_hp();
    double MP = max_mp();
    double STA = max_sta();

    Inventory inventory = new Inventory();

    Equipment equipment = new Equipment(inventory);

    ArrayList<Skill> skills = new ArrayList<>();

    public enum Race {
        HUMAN,
        DWARF,
        HALFLING,
        DEMON,
        SUCCUBUS,
        ELF,
        VAMPIRE,
        CAT,
        WOLF,
        DOG,
        RABBIT,
        HARPY,
        DEER,
        GOAT,
        CENTAUR,
        LIZARD,
        SERPENT,
        MERFOLK,
        GNOME,
        FAIRY,
        NYMPH,
        ANGEL,
        CELESTIAL,

        INSECT,
        BEAST,
        REPTILE,
        GOBLIN,
        ORC,
        TROLL,
        SKELETON,
        ZOMBIE,
        SLIME,
        ELEMENTAL,
        GOLEM,
        TREANT,
        STATUE,
        GIANT,
        GHOST,
        LICH,
        VOID_BEING,
        DRAGON,
        TITAN,
        PHOENIX,
        BEHEMOTH,
        LEVIATHAN,
        CHIMERA
    }

    public double[] world_cm = new double[]{0, 0, -400000};

    public Entity(String name, Race race) {
        this.name = name;
        this.race = race;
    }

    public int max_hp() {
        return (int) Math.ceil(5 * level + 5d * Math.pow(VIT, 1.5d));
    }

    public int max_mp() {
        return (int) Math.ceil(2 * level + 4d * Math.pow(INT + WIS, 1.5d));
    }

    public int max_sta() {
        return 10 * STR + 5 * DEX + 20 * VIT;
    }

    public int hp_regen() {
        return (int) Math.ceil(Math.log(level) + VIT);
    }

    public int mp_regen() {
        return (int) Math.ceil(2 * Math.log(level) + Math.pow(INT + WIS - 5, 1.3));
    }

    public int sta_regen() {
        return (int) Math.ceil((double) max_sta() / 10);
    }

    public void regen() {
        HP = Math.min(HP + hp_regen(), max_hp());
        MP = Math.min(MP + mp_regen(), max_mp());
        STA = Math.min(STA + sta_regen(), max_sta());
    }

    public void expend_sta(int sta) {
        STA = Math.max(0, STA - sta);
    }

    public void expend_mp(int mp) {
        MP = Math.max(0, MP - mp);
    }

    public void take_damage(int damage) {
        HP = Math.max(0, HP - damage);
    }

    public double dodge_chance() {
        return Math.max(0, 0.2 + 0.1 * ((double) level / (level + 20)) + 0.7 * ((double) DEX / (DEX + 20)) + 0.2 * ((double) STR / (STR + 100)) - Math.log(VIT - 4) / 5);
    }
}
