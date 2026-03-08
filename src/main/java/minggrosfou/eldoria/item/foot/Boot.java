package minggrosfou.eldoria.item.foot;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Boot extends Item {
    public Boot() {
        name = "Boot";
        description = "Basic boot";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.FOOT_LEFT, Equipment.Equip_Slot.FOOT_RIGHT};
        weight = 0.25;
        volume = 0.5;
    }
}
