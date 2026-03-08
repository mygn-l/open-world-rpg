package minggrosfou.eldoria.item.head;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Helmet extends Item {
    public Helmet() {
        name = "Helmet";
        description = "Basic helmet";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.HEAD};
        weight = 0.5;
        volume = 1;
    }
}
