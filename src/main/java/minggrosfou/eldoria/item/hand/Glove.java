package minggrosfou.eldoria.item.hand;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Glove extends Item {
    public Glove() {
        name = "Glove";
        description = "Basic glove";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.HAND_LEFT, Equipment.Equip_Slot.HAND_RIGHT};
        weight = 0;
        volume = 0;
    }
}
