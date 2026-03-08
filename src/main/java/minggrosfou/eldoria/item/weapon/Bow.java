package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Bow extends Item {
    public Bow() {
        name = "Bow";
        description = "Basic bow";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND};
        weight = 0.5;
        volume = 1;
    }
}
