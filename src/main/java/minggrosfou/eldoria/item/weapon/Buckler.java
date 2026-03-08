package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Buckler extends Item {
    public Buckler() {
        name = "Buckler";
        description = "Basic buckler";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND, Equipment.Equip_Slot.OFF_HAND};
        weight = 1;
        volume = 1;
    }
}
