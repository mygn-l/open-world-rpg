package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Tower_Shield extends Item {
    public Tower_Shield() {
        name = "Tower shield";
        description = "Basic tower shield";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND};
        weight = 3;
        volume = 3;
    }
}
