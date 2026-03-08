package minggrosfou.eldoria.item.upperbody;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Robe extends Item {
    public Robe() {
        name = "Robe";
        description = "Basic robe";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.UPPER_OUTER, Equipment.Equip_Slot.CHEST};
        weight = 0.25;
        volume = 0.25;
    }
}
