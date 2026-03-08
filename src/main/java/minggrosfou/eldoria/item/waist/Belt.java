package minggrosfou.eldoria.item.waist;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Belt extends Item {
    public Belt() {
        name = "Belt";
        description = "Basic belt";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.WAIST};
        weight = 0.25;
        volume = 0.25;
    }
}
