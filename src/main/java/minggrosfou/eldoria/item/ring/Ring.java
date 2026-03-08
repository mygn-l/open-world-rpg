package minggrosfou.eldoria.item.ring;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public abstract class Ring extends Item {
    public Ring() {
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.RING_LEFT, Equipment.Equip_Slot.RING_RIGHT};
        weight = 0;
        volume = 0;
    }
}
