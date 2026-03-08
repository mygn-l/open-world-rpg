package minggrosfou.eldoria.skill;

public class Fireball extends Skill{
    public Fireball() {
        type = Type.ACTIVE;
        name = "Fireball";
        description = "Shoot a fireball in a direction";
        CD_s = 2;
        MP_cost = 10;
    }
}
