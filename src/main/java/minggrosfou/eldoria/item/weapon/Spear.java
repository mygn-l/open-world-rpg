package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Spear extends Item {
    public Spear() {
        name = "Spear";
        description = "Basic spear";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND};
        weight = 1.5;
        volume = 2;
    }
}
