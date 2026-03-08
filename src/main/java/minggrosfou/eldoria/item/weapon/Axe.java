package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Axe extends Item {
    public Axe() {
        name = "Axe";
        description = "Basic axe";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND};
        weight = 2.5;
        volume = 1.5;
    }
}
