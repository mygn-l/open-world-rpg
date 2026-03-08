package minggrosfou.eldoria.item.upperbody;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Chestplate extends Item {
    public Chestplate() {
        name = "Chestplate";
        description = "Basic chestplate";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.UPPER_OUTER, Equipment.Equip_Slot.CHEST};
        weight = 2.5;
        volume = 3;
    }
}
