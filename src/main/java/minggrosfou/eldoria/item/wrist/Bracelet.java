package minggrosfou.eldoria.item.wrist;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public abstract class Bracelet extends Item {
    public Bracelet() {
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.WRIST_LEFT, Equipment.Equip_Slot.WRIST_RIGHT};
        weight = 0;
        volume = 0;
    }
}
