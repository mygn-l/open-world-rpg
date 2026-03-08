package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Dagger extends Item {
    public Dagger() {
        name = "Dagger";
        description = "Basic dagger";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND, Equipment.Equip_Slot.OFF_HAND};
        weight = 0.5;
        volume = 0.5;
    }
}
