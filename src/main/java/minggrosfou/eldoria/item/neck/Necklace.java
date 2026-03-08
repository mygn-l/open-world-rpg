package minggrosfou.eldoria.item.neck;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public abstract class Necklace extends Item {
    public Necklace() {
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.NECK};
        weight = 0;
        volume = 0;
    }
}
