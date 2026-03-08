package minggrosfou.eldoria.item.lowerbody;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Pants extends Item {
    public Pants() {
        name = "Pants";
        description = "Basic pants";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.LOWER_OUTER};
        weight = 0;
        volume = 0;
    }
}
