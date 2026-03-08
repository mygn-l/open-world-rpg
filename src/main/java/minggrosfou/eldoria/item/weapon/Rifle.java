package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Rifle extends Item {
    public Rifle() {
        name = "Rifle";
        description = "Basic rifle";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND};
        weight = 3;
        volume = 1.5;
    }
}
