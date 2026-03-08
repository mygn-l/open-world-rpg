package minggrosfou.eldoria.item.back;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Cloak extends Item {
    public Cloak() {
        name = "Cloak";
        description = "Basic cloak";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.BACK};
        weight = 0;
        volume = 0;
    }
}
