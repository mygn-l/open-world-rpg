package minggrosfou.eldoria.item.arm;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Armguard extends Item {
    public Armguard() {
        name = "Armguard";
        description = "Basic armguard";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.ARM_LEFT, Equipment.Equip_Slot.ARM_RIGHT};
        weight = 0.5;
        volume = 0.5;
    }
}
