package minggrosfou.eldoria.item.upperbody;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Tunic extends Item {
    public Tunic() {
        name = "Tunic";
        description = "Basic tunic";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.UPPER_OUTER, Equipment.Equip_Slot.CHEST};
        weight = 0.25;
        volume = 0.25;
    }
}
