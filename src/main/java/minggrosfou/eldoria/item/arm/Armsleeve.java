package minggrosfou.eldoria.item.arm;

import minggrosfou.eldoria.entity.Equipment;
import minggrosfou.eldoria.item.Item;

public class Armsleeve extends Item {
    public Armsleeve() {
        name = "Armsleeve";
        description = "Basic armsleeve";
        equippable = true;
        allowed_slots = new Equipment.Equip_Slot[]{Equipment.Equip_Slot.ARM_LEFT, Equipment.Equip_Slot.ARM_RIGHT};
        weight = 0;
        volume = 0;
    }
}
