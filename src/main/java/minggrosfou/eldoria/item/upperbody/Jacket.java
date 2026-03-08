package minggrosfou.eldoria.item.upperbody;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Jacket extends Item {
    public Jacket() {
        name = "Jacket";
        description = "Basic jacket";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.UPPER_OUTER, Equipment.Equip_Slot.CHEST};
        weight = 0.5;
        volume = 0.5;
    }
}
