package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Knuckle extends Item {
    public Knuckle() {
        name = "Knuckle";
        description = "Basic knuckle";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND, Equipment.Equip_Slot.OFF_HAND};
        weight = 0.25;
        volume = 0.25;
    }
}
