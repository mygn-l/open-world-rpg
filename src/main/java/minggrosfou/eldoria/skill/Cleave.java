package minggrosfou.eldoria.skill;

public class Cleave extends Skill{
    public Cleave() {
        type = Type.ACTIVE;
        name = "Cleave";
        description = "Hit multiple enemies simultaneously";
        CD_s = 5;
        MP_cost = 0;
    }
}
