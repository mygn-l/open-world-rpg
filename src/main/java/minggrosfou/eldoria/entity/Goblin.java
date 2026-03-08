package minggrosfou.eldoria.entity;

import minggrosfou.eldoria.item.currency.Bronze;
import minggrosfou.eldoria.item.weapon.Dagger;

public class Goblin extends Entity{
    public Goblin() {
        super("Goblin", Race.GOBLIN);

        backstory = "Low level goblin";

        level = 1;

        equipment.equip(new Dagger(), Equipment.Equip_Slot.MAIN_HAND);

        inventory.add_item(new Bronze());
    }
}
