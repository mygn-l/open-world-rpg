package minggrosfou.eldoria.item.weapon;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Pistol extends Item {
    public Pistol() {
        name = "Pistol";
        description = "Basic pistol";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.MAIN_HAND, Equipment.Equip_Slot.OFF_HAND};
        weight = 0.5;
        volume = 0.5;
    }
}
