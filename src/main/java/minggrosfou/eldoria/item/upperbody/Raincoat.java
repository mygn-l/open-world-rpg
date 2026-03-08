package minggrosfou.eldoria.item.upperbody;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Raincoat extends Item {
    public Raincoat() {
        name = "Raincoat";
        description = "Basic raincoat";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.UPPER_OUTER};
        weight = 0.25;
        volume = 0.25;
    }
}
