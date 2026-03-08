package minggrosfou.eldoria.item.ankle;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Sock extends Item {
    public Sock() {
        name = "Sock";
        description = "Basic sock";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.ANKLE_LEFT, Equipment.Equip_Slot.ANKLE_RIGHT};
        weight = 0;
        volume = 0;
    }
}
